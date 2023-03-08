package com.panpan.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.Query;
import com.panpan.gulimall.product.dao.SkuSaleAttrValueDao;
import com.panpan.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.panpan.gulimall.product.service.SkuSaleAttrValueService;
import com.panpan.gulimall.product.vo.thymeleafvo.SkuItemVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuItemVo.SkuItemSaleAttrVO> getSpuSaleAttrValueBySpuId(Long spuId) {
        return baseMapper.getSpuSaleAttrValueBySpuId(spuId);
    }

}