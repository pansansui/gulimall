package com.panpan.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panpan.common.utils.PageUtils;
import com.panpan.gulimall.product.entity.AttrEntity;
import com.panpan.gulimall.product.entity.ProductAttrValueEntity;
import com.panpan.gulimall.product.vo.AttrRespVo;
import com.panpan.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 15:34:39
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveVo(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, long catelogId, String attrType);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);

    List<AttrEntity> getRelationAttr(long attrGroupId);

    PageUtils surplusRelation(long attrGroupId, Map<String, Object> params);
//仅基础属性
    List<AttrEntity> getRelationAttrInOne(Long attrGroupId);
//    所有销售属性
    List<AttrEntity> getRelationSaleAttr(Long attrGroupId);
    //    所有基础属性
    List<AttrEntity> getRelationbaseAttr(Long attrGroupId);

    List<ProductAttrValueEntity> listForSpu(Long spuId);

    void saveBatchBySpuId(Long spuId, List<ProductAttrValueEntity> productAttrValueEntities);

    List<Long> listSearchableByIds(List<Long> attrIdList);
}

