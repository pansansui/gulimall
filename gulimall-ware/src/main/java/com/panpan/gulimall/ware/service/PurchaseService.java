package com.panpan.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panpan.common.utils.PageUtils;
import com.panpan.gulimall.ware.entity.PurchaseEntity;
import com.panpan.gulimall.ware.vo.MergeVo;
import com.panpan.gulimall.ware.vo.PurchaseCompleteVo;

import java.util.ArrayList;
import java.util.Map;

/**
 * 采购信息
 *
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 17:59:56
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnReceive(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);

    void receivePurchase(ArrayList<Long> purchaseIdList);

    void completePurchase(PurchaseCompleteVo purchaseCompleteVo);
}

