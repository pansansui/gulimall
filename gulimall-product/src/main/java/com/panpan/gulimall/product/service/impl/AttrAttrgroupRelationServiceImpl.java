package com.panpan.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.Query;
import com.panpan.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.panpan.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.panpan.gulimall.product.service.AttrAttrgroupRelationService;
import com.panpan.gulimall.product.vo.AttrAttrgroupRelationVo;
import com.panpan.gulimall.product.vo.thymeleafvo.SkuItemVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveBatch(AttrAttrgroupRelationVo[] relationVos) {
        List<AttrAttrgroupRelationEntity> relationEntityList = Arrays.stream(relationVos).map((item) -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        this.saveBatch(relationEntityList);
    }

    @Override
    public List<SkuItemVo.SpuAttrGroupVo> spuAttrGroupVoListBySpuId(Long spuId) {
        return baseMapper.spuAttrGroupVoListBySpuId(spuId);
    }
}
