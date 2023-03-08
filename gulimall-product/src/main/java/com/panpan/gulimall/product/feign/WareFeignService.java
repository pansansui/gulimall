package com.panpan.gulimall.product.feign;

import com.panpan.common.to.SkuStockTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author panpan
 * @create 2021-08-28 下午3:15
 */
@FeignClient("gulimall-ware")
public interface WareFeignService {
    @PostMapping("ware/waresku/hasStock")
    public List<SkuStockTo> hasStock(@RequestBody List<Long> skuIds);
}
