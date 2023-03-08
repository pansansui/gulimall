package com.panpan.gulimall.essearch.service;

import com.panpan.common.to.gulimallessearch.SkuEsModel;

import java.util.List;

/**
 * @author panpan
 * @create 2021-08-28 下午5:36
 */

public interface ProductSaveService {
    Boolean putProductSku(List<SkuEsModel> skuEsModels) ;
}
