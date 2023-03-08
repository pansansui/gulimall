package com.panpan.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author panpan
 * @create 2021-08-25 下午4:41
 */
@Data
public class MergeVo {
    private Long purchaseId;
    private List<Long> items;
}
