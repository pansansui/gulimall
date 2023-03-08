package com.panpan.gulimall.ware.fegin;
import com.panpan.common.to.SkuWareTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author panpan
 * @create 2021-08-26 下午8:31
 */
@FeignClient(value = "gulimall-product")
public interface GulimallProductFeignService {
    @RequestMapping("product/skuinfo/getskuto")
    public SkuWareTo getById(long skuId);
}
