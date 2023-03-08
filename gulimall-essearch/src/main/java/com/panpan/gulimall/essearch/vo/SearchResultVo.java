package com.panpan.gulimall.essearch.vo;

import com.panpan.common.to.gulimallessearch.SkuEsModel;
import lombok.Data;

import java.util.List;

/**
 * @author panpan
 * @create 2021-09-02 上午9:30
 */
@Data
public class SearchResultVo {
    private List<SkuEsModel>  products;
    // 分页信息
    private Integer pageNum;
    private Long total;
    private Integer totalPage;
    private List<BrandVo> brands;
    private List<attrVo> attrs;
    private List<catalogVo> catalogs;

    @Data
    public static class BrandVo{
        private Long brandId;
        private String brandName;
        private String brandImage;
    }

    @Data
    public static class attrVo{
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }
    @Data
    public static class catalogVo{
        private Long catalogId;
        private String catalogName;
        private String catalogImage;
    }
}
