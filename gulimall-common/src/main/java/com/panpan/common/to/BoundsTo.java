package com.panpan.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author panpan
 * @create 2021-08-24 下午7:55
 */
@Data
public class BoundsTo {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
