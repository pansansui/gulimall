package com.panpan.gulimall.product.dao;

import com.panpan.gulimall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 * 
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 15:34:39
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    List<Long> listSearchableByIds(@Param("attrIdList") List<Long> attrIdList);
}
