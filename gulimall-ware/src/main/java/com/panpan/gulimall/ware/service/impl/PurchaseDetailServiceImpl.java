package com.panpan.gulimall.ware.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.Query;

import com.panpan.gulimall.ware.dao.PurchaseDetailDao;
import com.panpan.gulimall.ware.entity.PurchaseDetailEntity;
import com.panpan.gulimall.ware.service.PurchaseDetailService;
import org.springframework.util.StringUtils;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                new QueryWrapper<PurchaseDetailEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> purchaseDetailEntityQueryWrapper = new QueryWrapper<>();
        String wareId = (String) params.get("wareId");
        if(!StringUtils.isEmpty(wareId)&&"0".equalsIgnoreCase(wareId)){
            purchaseDetailEntityQueryWrapper.eq("ware_id",wareId);
        }
        if(!StringUtils.isEmpty(params.get("status"))){
            purchaseDetailEntityQueryWrapper.eq("status",params.get("status"));
        }
        if(!StringUtils.isEmpty(params.get("key"))){
            purchaseDetailEntityQueryWrapper.and((wapper) -> {
                wapper.like("assignee_name",params.get("key"))
                        .or().like("phone",params.get("key"));
            });
        }

        IPage<PurchaseDetailEntity> page = this.page(new Query<PurchaseDetailEntity>().getPage(params), purchaseDetailEntityQueryWrapper);

        return new PageUtils(page);
    }

}