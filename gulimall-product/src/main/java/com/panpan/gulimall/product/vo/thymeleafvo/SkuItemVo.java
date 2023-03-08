package com.panpan.gulimall.product.vo.thymeleafvo;

import com.panpan.gulimall.product.entity.SkuImagesEntity;
import com.panpan.gulimall.product.entity.SkuInfoEntity;
import com.panpan.gulimall.product.entity.SpuInfoDescEntity;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author panpan
 * @create 2021-09-04 下午4:06
 */
@Data
public class SkuItemVo {
    private SkuInfoEntity skuInfo;
    private boolean hasStock=true;
    private List<SkuImagesEntity> skuImages;
    private List<SkuItemSaleAttrVO> saleAttrs;
    private SpuInfoDescEntity spuInfoDesc;
    private List<SpuAttrGroupVo> attrGroups;
    @ToString
    @Data
    public static class SkuItemSaleAttrVO{
        private Long attrId;
        private String attrName;
        private List<attrValueWithSkuId> attrValues;

    }
    @ToString
    @Data
    public static class attrValueWithSkuId{
        private List<Long> skuIds;
        private String attrValue;
    }

    @ToString
    @Data
    public static class SpuAttrGroupVo{
        private String groupName;
        private List<SpuBaseAttrVo> attrs;

    }
    @ToString
    @Data
    public static class SpuBaseAttrVo{
        private String attrName;
        private String attrValue;

    }
}
