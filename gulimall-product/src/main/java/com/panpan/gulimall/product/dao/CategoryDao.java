package com.panpan.gulimall.product.dao;

import com.panpan.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 15:34:39
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
