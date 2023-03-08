package com.panpan.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panpan.common.constant.ProductConst;
import com.panpan.common.to.BoundsTo;
import com.panpan.common.to.SkuCouponTo;
import com.panpan.common.to.SkuStockTo;
import com.panpan.common.to.gulimallessearch.SkuEsModel;
import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.Query;
import com.panpan.common.utils.R;
import com.panpan.gulimall.product.dao.SpuInfoDao;
import com.panpan.gulimall.product.entity.*;
import com.panpan.gulimall.product.feign.CouponFeignService;
import com.panpan.gulimall.product.feign.EsSearchFeignService;
import com.panpan.gulimall.product.feign.WareFeignService;
import com.panpan.gulimall.product.service.*;
import com.panpan.gulimall.product.vo.Images;
import com.panpan.gulimall.product.vo.SaveSPUVo;
import com.panpan.gulimall.product.vo.Skus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Autowired
    SpuImagesService spuImagesService;
    @Autowired
    SpuInfoDescService spuInfoDescService;
    @Autowired
    ProductAttrValueService productAttrValueService;
    @Autowired
    SkuInfoService skuInfoService;
    @Autowired
    SkuImagesService skuImagesService;
    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    CouponFeignService couponFeignService;
    @Autowired
    WareFeignService wareFeignService;
    @Autowired
    BrandService brandService;
    @Autowired
    AttrService  attrService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    EsSearchFeignService esSearchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }
    @Transactional
    @Override
    public void save(SaveSPUVo spuVo) {
//        1.保存spu基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuVo,spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.save(spuInfoEntity);
        Long spuId = spuInfoEntity.getId();
//        2.保存spu图片集 pms_spu_images
        spuImagesService.saveImages(spuId,spuVo.getImages());

//        3.保存spu描述图片 pms_spu_info_desc
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuId);
        spuInfoDescEntity.setDecript(String.join(",",spuVo.getDecript()));
        spuInfoDescService.save(spuInfoDescEntity);

//        4.保存spu规格参数 pms_product_attr_value
        productAttrValueService.saveAttrs(spuId,spuVo.getBaseAttrs());

//        5.保存spu积分信息 数据库：gulimall_sms 表 sms_spu_bounds
        BoundsTo boundsTo = new BoundsTo();
        BeanUtils.copyProperties(spuVo.getBounds(),boundsTo);
        boundsTo.setSpuId(spuId);
        R r = couponFeignService.save(boundsTo);
        if(r.getCode()!=0){
            log.error("保存spu积分信息(sms_spu_bounds)失败,spuId"+spuId);
        }

//        6.保存sku信息

//        6.1保存sku的基本信息 pms_sku_info

        List<Skus> skus = spuVo.getSkus();
        skus.stream().forEach((sku) -> {
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            BeanUtils.copyProperties(sku, skuInfoEntity);
            skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
            skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
            skuInfoEntity.setSaleCount(0l);
            skuInfoEntity.setSpuId(spuId);
            for (Images image : sku.getImages()) {
                if (image.getDefaultImg() == 1) {
                    skuInfoEntity.setSkuDefaultImg(image.getImgUrl());
                    break;
                }
            }
            skuInfoService.save(skuInfoEntity);
//        6.2 保存sku的图片信息 pms_sku_images
            List<SkuImagesEntity> imagesEntityList = sku.getImages().stream().map(images -> {
                SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                BeanUtils.copyProperties(images, skuImagesEntity);
                skuImagesEntity.setSkuId(skuInfoEntity.getSkuId());
                return skuImagesEntity;
            }).filter((image) ->
                 !StringUtils.isEmpty(image.getImgUrl())
            ).collect(Collectors.toList());
            skuImagesService.saveBatch(imagesEntityList);
//        6.3保存sku的销售属性 pms_sku_sale_attr_value
            List<SkuSaleAttrValueEntity> valueEntityList = sku.getAttr().stream().map((attr) -> {
                SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                BeanUtils.copyProperties(attr, skuSaleAttrValueEntity);
                skuSaleAttrValueEntity.setSkuId(skuInfoEntity.getSkuId());
                return skuSaleAttrValueEntity;
            }).collect(Collectors.toList());
            skuSaleAttrValueService.saveBatch(valueEntityList);
//        6.4保存sku的优惠信息 数据库：gulimall_sms 表 sms_sku_ladder  sms_sku_full_reduction member_price
            SkuCouponTo skuCouponTo = new SkuCouponTo();
            BeanUtils.copyProperties(sku,skuCouponTo);
            skuCouponTo.setSkuId(skuInfoEntity.getSkuId());
            R r1 = couponFeignService.saveSkuCouponTo(skuCouponTo);
            if(r1.getCode()!=0){
                log.error("保存sku优惠信息(sms_sku_ladder  sms_sku_full_reduction member_price)失败,skuId"+skuInfoEntity.getSkuId());
            }
        });

    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> spuInfoEntityQueryWrapper = new QueryWrapper<>();
        String catelogId = (String) params.get("catelogId");
        if(!StringUtils.isEmpty(catelogId)&&!"0".equalsIgnoreCase(catelogId)){
            spuInfoEntityQueryWrapper.eq("catalog_id", catelogId);
        }
        String brand_id = (String) params.get("brand_id");
        if(!StringUtils.isEmpty(brand_id)&& !"0".equalsIgnoreCase(brand_id)){
            spuInfoEntityQueryWrapper.eq("brand_id", brand_id);
        }
        if(!StringUtils.isEmpty(params.get("status"))){
            spuInfoEntityQueryWrapper.and((wapper) -> {
                wapper.eq("publish_status",params.get("status"));
            });
        }
        if(!StringUtils.isEmpty(params.get("key"))){
            spuInfoEntityQueryWrapper.and((wapper) -> {
                wapper.like("spu_name",params.get("key")).or().like("spu_description",params.get("key"));
            });
        }

        IPage<SpuInfoEntity> page = this.page(new Query<SpuInfoEntity>().getPage(params), spuInfoEntityQueryWrapper);


        return new PageUtils(page);
    }
/*
* @Description:
* @ParamType:[java.lang.Long]
* @Param: spuId
* @return: null
* @Author: panpan
* @Date:28/8/2021
*/
    @Transactional
    @Override
    public void putSpuById(Long spuId) {
        List<ProductAttrValueEntity> attrValueList = productAttrValueService.list(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        List<Long> attrIdList =attrValueList.stream()
                .map((item) -> {
                    return item.getAttrId();
                }).collect(Collectors.toList());
        HashSet<Long> longs = new HashSet<>(attrService.listSearchableByIds(attrIdList));
        List<SkuEsModel.Attrs> attrsList = attrValueList.stream()
                .filter((item) -> {
                    return longs.contains(item.getAttrId());
                })
                .map((item) -> {
                    SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
                    attrs.setAttrId(item.getAttrId());
                    attrs.setAttrName(item.getAttrName());
                    attrs.setAttrValue(item.getAttrValue());
                    return attrs;
                }).collect(Collectors.toList());

        List<SkuInfoEntity> skuInfoEntities = skuInfoService.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        List<Long> skuIds = skuInfoEntities.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        List<SkuStockTo> data=null;
        try {
            data =wareFeignService.hasStock(skuIds);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        Map<Long, Boolean> finalStockMap = data.stream().collect(Collectors.toMap(SkuStockTo::getSkuId, SkuStockTo::getHasStock));
        List<SkuEsModel> skuEsModels = skuInfoEntities.stream().map((item) -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(item, skuEsModel);
            skuEsModel.setCatalogId(item.getCatalogId());
            skuEsModel.setSkuPrice(item.getPrice());
            skuEsModel.setSkuImage(item.getSkuDefaultImg());
            //  28/8/2021 1.远程查仓库是否有库存
            skuEsModel.setHasStock(finalStockMap.get(item.getSkuId())==null?false:finalStockMap.get(item.getSkuId()));

            //  28/8/2021 2.热度评分，刚上架的为0
            skuEsModel.setHotScore(0l);
            //  28/8/2021 3.品牌和分类的名字
            BrandEntity brandEntity = brandService.getById(skuEsModel.getBrandId());
            skuEsModel.setBrandName(brandEntity.getName());
            skuEsModel.setBrandImage(brandEntity.getLogo());

            CategoryEntity categoryEntity = categoryService.getById(skuEsModel.getCatalogId());
            skuEsModel.setCatalogName(categoryEntity.getName());
            //  28/8/2021 4.当前sku下可被检索（search_type=1）的规格属性
            skuEsModel.setAttrs(attrsList);


            /*
            单独处理
            SkuPrice,setSkuImage,hasStock,hotScore
             private String brandName;
             private String brandImage;
             private String catelogName;
             public static class Attrs{
                private Long attrId;
                private String attrName;
                private String attrValue;
    }
             */
            return skuEsModel;
        }).collect(Collectors.toList());
        //  28/8/2021 发给es保存;
        R result = esSearchFeignService.putProductSku(skuEsModels);
        switch (result.getCode()){
            case 0:
                SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
                spuInfoEntity.setId(spuId);
                spuInfoEntity.setPublishStatus(ProductConst.SpuInfoPublishStatus.SPU_UP.getCode());
                spuInfoEntity.setUpdateTime(new Date());
                baseMapper.updateById(spuInfoEntity);
            default:
                // TODO: 28/8/2021 重复调用？
//                fegin有重试机制，默认关闭
              /*  while (true){
                    try {
                        executeAndDecode(template)
                    }catch (Exception e){
                        try{retryer.continueOrPropagate(e)}
                        catch(Exception ex){
                        throw ex}
                        continue;
                    }
                }*/
        }

    }


}