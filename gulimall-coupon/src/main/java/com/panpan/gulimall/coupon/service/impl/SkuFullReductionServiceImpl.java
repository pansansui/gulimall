package com.panpan.gulimall.coupon.service.impl;

import com.panpan.common.to.SkuCouponTo;
import com.panpan.gulimall.coupon.entity.MemberPriceEntity;
import com.panpan.gulimall.coupon.entity.SkuLadderEntity;
import com.panpan.gulimall.coupon.service.MemberPriceService;
import com.panpan.gulimall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.Query;

import com.panpan.gulimall.coupon.dao.SkuFullReductionDao;
import com.panpan.gulimall.coupon.entity.SkuFullReductionEntity;
import com.panpan.gulimall.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {
    @Autowired
    private SkuLadderService skuLadderService;
    @Autowired
    private MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void save(SkuCouponTo skuCouponTo) {
        long skuId = skuCouponTo.getSkuId();
//        保存sku ladder表
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuId);
        skuLadderEntity.setFullCount(skuCouponTo.getFullCount());
        skuLadderEntity.setDiscount(skuCouponTo.getDiscount());
        skuLadderEntity.setAddOther(skuCouponTo.getCountStatus());
        skuLadderService.save(skuLadderEntity);

//        保存sku full reduction
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuCouponTo,skuFullReductionEntity);
        skuFullReductionEntity.setAddOther(skuCouponTo.getPriceStatus());
        this.save(skuFullReductionEntity);

//        保存会员价 member price
        List<MemberPriceEntity> memberPriceEntityList = skuCouponTo.getMemberPrice().stream().map((item) -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setSkuId(skuId);
            memberPriceEntity.setMemberPrice(item.getPrice());
            memberPriceEntity.setMemberLevelName(item.getName());
            memberPriceEntity.setMemberLevelId(item.getId());
            memberPriceEntity.setAddOther(1);
            return memberPriceEntity;
        }).collect(Collectors.toList());
        memberPriceService.saveBatch(memberPriceEntityList);

    }

}