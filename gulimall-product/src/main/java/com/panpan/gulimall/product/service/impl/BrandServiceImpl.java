package com.panpan.gulimall.product.service.impl;

import com.panpan.gulimall.product.service.CategoryBrandRelationService;
import org.checkerframework.checker.units.qual.K;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.Query;

import com.panpan.gulimall.product.dao.BrandDao;
import com.panpan.gulimall.product.entity.BrandEntity;
import com.panpan.gulimall.product.service.BrandService;
import org.springframework.util.StringUtils;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {
    @Autowired
    private CategoryBrandRelationService relationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String)params.get("key");
        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(key)){
            wrapper.and((item)->{
                item.eq("brand_id",key).or().like("name",key).or().like("descript",key);
            });
        }
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void updateAndRelation(BrandEntity brand) {
        baseMapper.updateById(brand);
        if(!StringUtils.isEmpty(brand.getName())){
            relationService.updateBrand(brand.getBrandId(),brand.getName());
        }
        // TODO: 4/6/2021 更新其它的brand关联 
    }

}