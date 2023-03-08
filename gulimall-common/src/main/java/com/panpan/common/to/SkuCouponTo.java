package com.panpan.common.to;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author panpan
 * @create 2021-08-24 下午8:07
 */
@Data
public class SkuCouponTo {
    private long skuId;
    //    sku ladder
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    //    ske full reduction
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    //    member price
    private List<MemberPrice> memberPrice;


    private int priceStatus;
}
