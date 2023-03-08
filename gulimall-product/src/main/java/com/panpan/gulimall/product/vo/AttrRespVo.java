package com.panpan.gulimall.product.vo;

import lombok.Data;

/**
 * @author panpan
 * @create 2021-06-06 下午8:41
 */
@Data
public class AttrRespVo extends AttrVo{
/*    "catelogName": "手机/数码/手机",
      "groupName":*/
    private String catelogName;
    private String groupName;
    private Long[] catelogPath;
}
