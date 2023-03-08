package com.panpan.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panpan.common.utils.PageUtils;
import com.panpan.gulimall.product.entity.CategoryEntity;
import com.panpan.gulimall.product.vo.thymeleafvo.Catalog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 15:34:39
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeIfNotConnectionByIds(List<Long> catIds);

    Long[] getCatelogPath(Long catelogId);

    void updatedetailsById(CategoryEntity category);

    List<CategoryEntity> getCategorysForLevel1();

    Map<String, List<Catalog2Vo>> getCategorysGTLevel1();



}

