/**
  * Copyright 2021 json.cn 
  */
package com.panpan.gulimall.ware.vo;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Auto-generated: 2021-08-26 12:26:21
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
@Data
public class PurchaseCompleteVo {
    @NotNull
    private Long id;
    private List<PurchaseItemCompleteVo> items;


}