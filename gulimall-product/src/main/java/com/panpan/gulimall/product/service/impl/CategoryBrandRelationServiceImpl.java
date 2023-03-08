package com.panpan.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.panpan.gulimall.product.dao.BrandDao;
import com.panpan.gulimall.product.dao.CategoryDao;
import com.panpan.gulimall.product.entity.BrandEntity;
import com.panpan.gulimall.product.entity.CategoryEntity;
import com.panpan.gulimall.product.vo.CategoryBrandRelationVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.Query;

import com.panpan.gulimall.product.dao.CategoryBrandRelationDao;
import com.panpan.gulimall.product.entity.CategoryBrandRelationEntity;
import com.panpan.gulimall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private BrandDao brandDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetails(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        BrandEntity brandEntity = brandDao.selectById(brandId);
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        this.save(categoryBrandRelation);
    }

    @Override
    public void updateCategory(@Param("catId") Long catId, @Param("name") String name) {
        baseMapper.updateCategory(catId,name);
    }

    @Override
    public void updateBrand(Long brandId, String name) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setBrandName(name);
        categoryBrandRelationEntity.setBrandId(brandId);
        baseMapper.update(categoryBrandRelationEntity,new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_id",brandId));
    }

    @Override
    public List<CategoryBrandRelationVo> relationBrands(Long catelogId) {
        List<CategoryBrandRelationEntity> entityList = baseMapper.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_Id", catelogId));
        List<CategoryBrandRelationVo> brandRelationVoList = entityList.stream().map((item) -> {
            item.setCatelogName(null);
            item.setCatelogId(null);
            item.setId(null);
            CategoryBrandRelationVo brandRelationVo = new CategoryBrandRelationVo();
            BeanUtils.copyProperties(item,brandRelationVo);
            return brandRelationVo;
        }).collect(Collectors.toList());
        return brandRelationVoList;
    }

}