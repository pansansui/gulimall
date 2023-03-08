package com.panpan.common.to;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author panpan
 * @create 2021-08-26 下午8:26
 */
@Data
public class SkuWareTo {
    /**
     * skuId
     */
    @NotNull
    private Long skuId;
    /**
     * sku名称
     */
    private String skuName;
    /**
     * 价格
     */
    private BigDecimal price;

}
