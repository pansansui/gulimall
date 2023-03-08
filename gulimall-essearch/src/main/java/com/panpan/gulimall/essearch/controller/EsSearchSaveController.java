package com.panpan.gulimall.essearch.controller;

import com.panpan.common.exceptionenum.ExceptionNumber;
import com.panpan.common.to.gulimallessearch.SkuEsModel;
import com.panpan.common.utils.R;
import com.panpan.gulimall.essearch.service.ProductSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author panpan
 * @create 2021-08-28 下午5:30
 */
@RestController
@RequestMapping("search/save")
public class EsSearchSaveController {
    @Autowired
    private ProductSaveService productSaveService;
    @RequestMapping("/product")
    public R putProductSku(@RequestBody List<SkuEsModel> skuEsModels){
        Boolean isSuccess= productSaveService.putProductSku(skuEsModels);
        return isSuccess?R.ok():R.error(ExceptionNumber.PUT_ESSKU_EXCEPTHION.getCode(),ExceptionNumber.PUT_ESSKU_EXCEPTHION.getMessage());
    }
}
