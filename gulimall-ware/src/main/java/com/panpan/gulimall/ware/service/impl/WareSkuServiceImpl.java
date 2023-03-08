package com.panpan.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panpan.common.to.SkuStockTo;
import com.panpan.common.to.SkuWareTo;
import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.Query;
import com.panpan.gulimall.ware.dao.WareSkuDao;
import com.panpan.gulimall.ware.entity.WareSkuEntity;
import com.panpan.gulimall.ware.fegin.GulimallProductFeignService;
import com.panpan.gulimall.ware.service.PurchaseDetailService;
import com.panpan.gulimall.ware.service.WareSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
    @Autowired
    PurchaseDetailService purchaseDetailService;
    @Autowired
    GulimallProductFeignService gulimallProductFeignService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Integer skuNum, Long wareId) {
        List<WareSkuEntity> list = list(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if(list==null||list.size()<1){
            SkuWareTo skuTo = gulimallProductFeignService.getById(skuId);
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setSkuName(skuTo.getSkuName());
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setStockLocked(0);
            this.save(wareSkuEntity);
        }else{
            baseMapper.addStock(skuId,skuNum,wareId);
        }
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> wareSkuEntityQueryWrapper = new QueryWrapper<>();
        String wareId = (String) params.get("wareId");
        if(!StringUtils.isEmpty(wareId )&&!"0".equalsIgnoreCase(wareId)){
            wareSkuEntityQueryWrapper.eq("ware_id",wareId);
        }
        String skuId = (String) params.get("skuId");
        if(!StringUtils.isEmpty(skuId)&&!"0".equalsIgnoreCase(skuId)){
            wareSkuEntityQueryWrapper.eq("sku_id",skuId);
        }
        IPage<WareSkuEntity> page = this.page(new Query<WareSkuEntity>().getPage(params), wareSkuEntityQueryWrapper);

        return new PageUtils(page);
    }

    @Override
    public List<SkuStockTo> hasStock(List<Long> skuIds) {
        return skuIds.stream().map((item) -> {
            SkuStockTo skuStockTo = new SkuStockTo();
            skuStockTo.setSkuId(item);
            Long stock = baseMapper.hasStock(item)==null?0:baseMapper.hasStock(item);
            skuStockTo.setHasStock(stock>0);
            return skuStockTo;
        }).collect(Collectors.toList());
    }

}