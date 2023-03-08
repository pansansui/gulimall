package com.panpan.gulimall.product.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author panpan
 * @create 2021-08-15 上午11:44
 */
@Data
public class CategoryBrandRelationVo {
    /**
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    /**
     * 品牌id
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long brandId;
    /**
     * 分类id
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long catelogId;
    /**
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String brandName;
    /**
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String catelogName;

}
