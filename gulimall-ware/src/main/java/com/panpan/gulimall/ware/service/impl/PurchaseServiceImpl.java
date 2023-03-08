package com.panpan.gulimall.ware.service.impl;

import com.panpan.common.constant.WareConst;
import com.panpan.gulimall.ware.entity.PurchaseDetailEntity;
import com.panpan.gulimall.ware.entity.WareSkuEntity;
import com.panpan.gulimall.ware.service.PurchaseDetailService;
import com.panpan.gulimall.ware.service.WareSkuService;
import com.panpan.gulimall.ware.vo.MergeVo;
import com.panpan.gulimall.ware.vo.PurchaseCompleteVo;
import com.panpan.gulimall.ware.vo.PurchaseItemCompleteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.Query;

import com.panpan.gulimall.ware.dao.PurchaseDao;
import com.panpan.gulimall.ware.entity.PurchaseEntity;
import com.panpan.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    @Autowired
    PurchaseDetailService purchaseDetailService;
    @Autowired
    WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnReceive(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status",0).or().eq("status",1)
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        List<Long> items = mergeVo.getItems();
        if(purchaseId>0l){
            PurchaseEntity purchaseEntity = this.baseMapper.selectById(purchaseId);
            if(purchaseEntity.getStatus()<2){
                // 合并操作
                mergePurchase(purchaseId,items);
                purchaseEntity.setUpdateTime(new Date());
                this.updateById(purchaseEntity);
            }else throw new RuntimeException("非法的采购单");
        }else if(purchaseId==null||purchaseId==0) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConst.PurchaseStatusEnum.CREATED.getCode());
            this.save(purchaseEntity);
            mergePurchase(purchaseEntity.getId(),items);
        }

    }
   /*
   * @Description:
   * @ParamType:[java.util.ArrayList<java.lang.Long>]
   * @Param: purchaseIdList
   * @return: null
   * @Author: panpan
   * @Date:26/8/2021
   */
    @Override
    @Transactional
    public void receivePurchase(ArrayList<Long> purchaseIdList) {
        List<PurchaseEntity> purchaseEntityList = this.listByIds(purchaseIdList);
        // 选取正常status的entity
        List<PurchaseEntity> purchaseEntities = purchaseEntityList.stream().filter((entity) -> {
            boolean isValid=entity.getStatus() == WareConst.PurchaseStatusEnum.CREATED.getCode()
                    || entity.getStatus() == WareConst.PurchaseStatusEnum.ALLOCATED.getCode();
                    if(isValid){
                        entity.setStatus(WareConst.PurchaseStatusEnum.RECEIVE.getCode());
                        entity.setUpdateTime(new Date());
                    }
            return isValid;
        }).collect(Collectors.toList());


        if(purchaseEntities!=null&&purchaseEntities.size()>0){
            this.updateBatchById(purchaseEntities);
            List<Long> validPurchaseIdList = purchaseEntities.stream().map((entity) -> {
                return entity.getId();
            }).distinct().collect(Collectors.toList());
            List<PurchaseDetailEntity> detailEntities = purchaseDetailService.list(new QueryWrapper<PurchaseDetailEntity>().in("purchase_id", validPurchaseIdList));
            List<PurchaseDetailEntity> validDetailEntities = detailEntities.stream().filter((entity) -> {
                boolean isValid = entity.getStatus() == WareConst.PurchaseDetailStatusEnum.CREATED.getCode()
                        || entity.getStatus() == WareConst.PurchaseDetailStatusEnum.ALLOCATED.getCode()
                        || entity.getStatus() == WareConst.PurchaseDetailStatusEnum.HASERROR.getCode();
                if (isValid) {
                    entity.setStatus(WareConst.PurchaseDetailStatusEnum.PURCHASEING.getCode());
                }
                return isValid;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(validDetailEntities);
        }




    }
    /*
    * @Description:
    * @ParamType:PurchaseCompleteVo
    * @Param: purchaseCompleteVo
    * @return: null
    * @Author: panpan
    * @Date:26/8/2021
    */
    @Transactional
    @Override
    public void completePurchase(PurchaseCompleteVo purchaseCompleteVo) {
        if(purchaseCompleteVo.getId()>0&&purchaseCompleteVo.getItems()!=null&&purchaseCompleteVo.getItems().size()>0){
            PurchaseEntity purchaseEntity = this.getById(purchaseCompleteVo.getId());
            if(purchaseEntity==null){return;}
            // 保存订单具体项目，并判断是否有错
            boolean isError=false;
            for (PurchaseItemCompleteVo item : purchaseCompleteVo.getItems()) {
                if(item.getStatus()!=WareConst.PurchaseDetailStatusEnum.COMPLETED.getCode()){
                    isError=true;
                    // 改变购买有错误操作的购买商品项的状态status
                    PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                    purchaseDetailEntity.setId(item.getItemId());
                    purchaseDetailEntity.setStatus(WareConst.PurchaseDetailStatusEnum.HASERROR.getCode());
                    purchaseDetailService.updateById(purchaseDetailEntity);
                }else {
                    PurchaseDetailEntity purchaseDetailEntity = purchaseDetailService.getById(item.getItemId());
                    if(purchaseDetailEntity.getStatus() != WareConst.PurchaseDetailStatusEnum.PURCHASEING.getCode()){
                        isError = true;
                        // 改变数据库信息有错误的购买商品项的状态status
                        purchaseDetailEntity.setStatus(WareConst.PurchaseDetailStatusEnum.HASERROR.getCode());

                    } else {
                        //保存  传入是完成，数据库中是正在购买的商品项
                        purchaseDetailEntity.setStatus(WareConst.PurchaseDetailStatusEnum.COMPLETED.getCode());
                        // 改变库存
                        wareSkuService.addStock(purchaseDetailEntity.getSkuId(),purchaseDetailEntity.getSkuNum(),purchaseDetailEntity.getWareId());

                    }
                    purchaseDetailService.updateById(purchaseDetailEntity);
                }
            }
            // 保存采购单信息
            purchaseEntity.setStatus(isError?WareConst.PurchaseStatusEnum.HASERROR.getCode() : WareConst.PurchaseStatusEnum.COMPLETED.getCode());
            this.updateById(purchaseEntity);
        }

    }
    @Transactional
    public void  mergePurchase(Long purchaseId,List<Long> items){
        // 更新wms_purchase_detail
        if(items!=null&&items.size()>0){
            List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailService.listByIds(items);
            List<PurchaseDetailEntity> newEntityList = purchaseDetailEntities.stream().filter((entity) -> {
                return entity.getStatus() == WareConst.PurchaseDetailStatusEnum.CREATED.getCode() ||
                        entity.getStatus() == WareConst.PurchaseDetailStatusEnum.HASERROR.getCode();
            }).map((entity) -> {
                entity.setPurchaseId(purchaseId);
                entity.setStatus(WareConst.PurchaseDetailStatusEnum.ALLOCATED.getCode());
                return entity;
            }).collect(Collectors.toList());
            if(newEntityList!=null&&newEntityList.size()>0){
                purchaseDetailService.updateBatchById(newEntityList);
            }
        }

    }
}