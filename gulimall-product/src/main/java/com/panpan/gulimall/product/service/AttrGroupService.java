package com.panpan.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panpan.common.utils.PageUtils;
import com.panpan.gulimall.product.entity.AttrGroupEntity;
import com.panpan.gulimall.product.vo.AttrAttrgroupRelationVo;
import com.panpan.gulimall.product.vo.AttrgroupsWithAttrsVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 15:34:39
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    void removeRelation(AttrAttrgroupRelationVo[] vos);

    List<AttrgroupsWithAttrsVo> attrgroupsWithAttrsByCatelogId(Long catelogId);

    List<AttrgroupsWithAttrsVo> attrgroupsWithAttrsInOneByCatelogId(Long catelogId);
}

