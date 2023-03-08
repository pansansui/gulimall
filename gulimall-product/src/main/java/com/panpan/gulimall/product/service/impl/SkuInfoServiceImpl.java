package com.panpan.gulimall.product.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.Query;
import com.panpan.gulimall.product.dao.SkuInfoDao;
import com.panpan.gulimall.product.entity.SkuImagesEntity;
import com.panpan.gulimall.product.entity.SkuInfoEntity;
import com.panpan.gulimall.product.service.*;
import com.panpan.gulimall.product.vo.thymeleafvo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {
    @Autowired
    SkuImagesService skuImagesService;
    @Autowired
    SpuInfoDescService spuInfoDescService;
    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    AttrAttrgroupRelationService attrAttrgroupRelationService;
    @Autowired
    ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> skuInfoEntityQueryWrapper = new QueryWrapper<>();
        String catelogId = (String) params.get("catelogId");
        if(!StringUtils.isEmpty(catelogId)&&!"0".equalsIgnoreCase(catelogId)){
            skuInfoEntityQueryWrapper.eq("catalog_id", catelogId);
        }
        String brand_id = (String) params.get("brand_id");
        if(!StringUtils.isEmpty(brand_id)&& !"0".equalsIgnoreCase(brand_id)){
            skuInfoEntityQueryWrapper.eq("brand_id", brand_id);
        }
        String min = (String) params.get("min");
        if(!StringUtils.isEmpty(min)&& !"0".equalsIgnoreCase(min)){
            skuInfoEntityQueryWrapper.ge("price", min);
        }
        String max = (String) params.get("max");
        if(!StringUtils.isEmpty(max)&& !"0".equalsIgnoreCase(max)){
            skuInfoEntityQueryWrapper.le("price", max);
        }

        if(!StringUtils.isEmpty(params.get("key"))){
            skuInfoEntityQueryWrapper.and((wapper) -> {
                wapper.like("sku_name",params.get("key"))
                        .or().like("sku_desc",params.get("key"))
                        .or().like("sku_subtitle",params.get("key"))
                        .or().like("sku_title",params.get("key"));
            });
        }
        IPage<SkuInfoEntity> page = this.page(new Query<SkuInfoEntity>().getPage(params), skuInfoEntityQueryWrapper);

        return new PageUtils(page);
    }

    @Override
    public SkuItemVo getSkuItemVo(Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo = new SkuItemVo();
        CompletableFuture<Void> skuInfo= CompletableFuture.runAsync(() -> {
            skuItemVo.setSkuInfo(getById(skuId));
        });

        CompletableFuture<Void> sku_Image = CompletableFuture.runAsync(() -> {
            skuItemVo.setSkuImages(skuImagesService.list(new QueryWrapper<SkuImagesEntity>().eq("sku_id", skuId)));
        });


        CompletableFuture<Void> voidCompletableFuture = skuInfo.thenAcceptAsync((item) -> {
            System.out.println(Thread.currentThread().getName());
            skuItemVo.setSpuInfoDesc(spuInfoDescService.getById(skuItemVo.getSkuInfo().getSpuId()));
        });

        CompletableFuture<Void> voidCompletableFuture1 = skuInfo.thenAcceptAsync((item) -> {
            skuItemVo.setSaleAttrs(skuSaleAttrValueService.getSpuSaleAttrValueBySpuId(skuItemVo.getSkuInfo().getSpuId()));
        });

        CompletableFuture<Void> voidCompletableFuture2 = skuInfo.thenAcceptAsync((item) -> {
            skuItemVo.setAttrGroups(attrAttrgroupRelationService.spuAttrGroupVoListBySpuId(skuItemVo.getSkuInfo().getSpuId()));
        });

        CompletableFuture.allOf(sku_Image,voidCompletableFuture,voidCompletableFuture1,voidCompletableFuture2).get();
        // skuInfo
//        skuItemVo.setSkuInfo(getById(skuId));
        // skuImage
//        skuItemVo.setSkuImages(skuImagesService.list(new QueryWrapper<SkuImagesEntity>().eq("sku_id",skuId)));
        // spuDesc
//        skuItemVo.setSpuInfoDesc(spuInfoDescService.getById(skuItemVo.getSkuInfo().getSpuId()));
        // sku sale attr
//        skuItemVo.setSaleAttrs(skuSaleAttrValueService.getSpuSaleAttrValueBySpuId(skuItemVo.getSkuInfo().getSpuId()));
        // spu 关联的所有属性
//        skuItemVo.setAttrGroups(attrAttrgroupRelationService.spuAttrGroupVoListBySpuId(skuItemVo.getSkuInfo().getSpuId()));
        return skuItemVo;
    }


}