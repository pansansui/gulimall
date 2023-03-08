package com.panpan.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panpan.common.to.SkuStockTo;
import com.panpan.common.utils.PageUtils;
import com.panpan.gulimall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 17:59:56
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId,Integer skuNum,Long wareId);

    PageUtils queryPageByCondition(Map<String, Object> params);

    List<SkuStockTo> hasStock(List<Long> skuIds);

}

