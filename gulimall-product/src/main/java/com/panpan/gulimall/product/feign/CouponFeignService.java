package com.panpan.gulimall.product.feign;

import com.panpan.common.to.BoundsTo;
import com.panpan.common.to.SkuCouponTo;
import com.panpan.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author panpan
 * @create 2021-08-24 下午7:31
 */
@FeignClient(value = "gulimall-coupon")
public interface CouponFeignService {
    @RequestMapping("coupon/spubounds/save")
    R save(@RequestBody BoundsTo boundsTo);

    @PostMapping("coupon/skufullreduction/saveSkuCouponTo")
    R saveSkuCouponTo(@RequestBody SkuCouponTo skuCouponTo);
}
