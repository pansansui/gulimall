package com.panpan.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panpan.common.utils.PageUtils;
import com.panpan.gulimall.product.entity.CategoryBrandRelationEntity;
import com.panpan.gulimall.product.vo.CategoryBrandRelationVo;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 15:34:39
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetails(CategoryBrandRelationEntity categoryBrandRelation);

    void updateCategory(Long catId, String name);

    void updateBrand(Long brandId, String name);

    List<CategoryBrandRelationVo> relationBrands(Long catelogId);


}

