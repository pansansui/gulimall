package com.panpan.gulimall.product.vo;

import com.panpan.gulimall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @author panpan
 * @create 2021-08-15 下午3:17
 */
@Data
public class AttrgroupsWithAttrsVo {

    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    private List<AttrEntity> attrs;
}
