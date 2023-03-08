package com.panpan.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panpan.common.utils.PageUtils;
import com.panpan.gulimall.product.entity.SkuInfoEntity;
import com.panpan.gulimall.product.vo.thymeleafvo.SkuItemVo;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 15:34:39
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);


    PageUtils queryPageByCondition(Map<String, Object> params);

    SkuItemVo getSkuItemVo(Long skuId) throws ExecutionException, InterruptedException;
}

