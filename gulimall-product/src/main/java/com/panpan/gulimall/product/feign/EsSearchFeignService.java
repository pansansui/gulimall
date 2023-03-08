package com.panpan.gulimall.product.feign;

import com.panpan.common.to.gulimallessearch.SkuEsModel;
import com.panpan.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author panpan
 * @create 2021-08-28 下午8:00
 */
@FeignClient("gulimall-essearch")
public interface EsSearchFeignService {
    @RequestMapping("/search/save/product")
    public R putProductSku(@RequestBody List<SkuEsModel> skuEsModels);
}
