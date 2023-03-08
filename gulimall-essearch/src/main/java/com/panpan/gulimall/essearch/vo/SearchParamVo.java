package com.panpan.gulimall.essearch.vo;

import lombok.Data;

import java.util.List;

/**
 * @author panpan
 * @create 2021-09-01 下午9:32
 */
@Data
public class SearchParamVo {
    private Long catalog3Id;
    private String keyword;

    // 排序条件 skuPrice_asc saleCount_desc hotScore_asc;
    private String sort;
    // 过滤条件
    // 1.库存
    private Integer hasStock=1;
    // 2.skuPrice=1_300
    private  String skuPrice;
    // 3.品牌
    private List<Long> brandIds;
    // 4.attrs=1_5寸：6寸
    private List<String> attrs;
    //-------------------------------------------------
    // 页码
    private Integer pageNum=1;
}
