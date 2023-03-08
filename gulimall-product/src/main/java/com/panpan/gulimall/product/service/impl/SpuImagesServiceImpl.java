package com.panpan.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.Query;

import com.panpan.gulimall.product.dao.SpuImagesDao;
import com.panpan.gulimall.product.entity.SpuImagesEntity;
import com.panpan.gulimall.product.service.SpuImagesService;
import org.springframework.util.CollectionUtils;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveImages(Long spuId, List<String> images) {
        if(CollectionUtils.isEmpty(images)){
            return;
        }else{
        List<SpuImagesEntity> collect = images.stream().map((item) -> {
            SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
            spuImagesEntity.setImgUrl(item);
            spuImagesEntity.setSpuId(spuId);
            return spuImagesEntity;
        }).collect(Collectors.toList());
            this.saveBatch(collect);
        }

    }

}