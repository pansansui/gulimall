package com.panpan.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panpan.common.utils.PageUtils;
import com.panpan.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.panpan.gulimall.product.vo.AttrAttrgroupRelationVo;
import com.panpan.gulimall.product.vo.thymeleafvo.SkuItemVo;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 15:34:39
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveBatch(AttrAttrgroupRelationVo[] relationVos);

    List<SkuItemVo.SpuAttrGroupVo> spuAttrGroupVoListBySpuId(Long spuId);
}

