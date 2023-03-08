package com.panpan.gulimall.product.dao;

import com.panpan.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panpan.gulimall.product.vo.AttrAttrgroupRelationVo;
import com.panpan.gulimall.product.vo.thymeleafvo.SkuItemVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 15:34:39
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    void removeBatchRelaton(@Param("vos") AttrAttrgroupRelationVo[] vos);

    List<SkuItemVo.SpuAttrGroupVo> spuAttrGroupVoListBySpuId(@Param("spuId") Long spuId);
}
