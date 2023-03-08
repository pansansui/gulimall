package com.panpan.gulimall.ware.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import com.panpan.gulimall.ware.vo.MergeVo;
import com.panpan.gulimall.ware.vo.PurchaseCompleteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.panpan.gulimall.ware.entity.PurchaseEntity;
import com.panpan.gulimall.ware.service.PurchaseService;
import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.R;



/**
 * 采购信息
 *
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 17:59:56
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }

    @RequestMapping("/received")
    public R receive(@RequestBody ArrayList<Long> purchaseIdList){
        purchaseService.receivePurchase(purchaseIdList);
        return R.ok();
    }
    @RequestMapping("/unreceive/list")
    public R listUnReceive(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPageUnReceive(params);

        return R.ok().put("page", page);
    }
    @RequestMapping("/merge")
    public R merge(@RequestBody MergeVo mergeVo){
        purchaseService.mergePurchase(mergeVo);

        return R.ok();
    }

    @RequestMapping("/done")
    public R finish(@RequestBody PurchaseCompleteVo purchaseCompleteVo){
        purchaseService.completePurchase(purchaseCompleteVo);

        return R.ok();
    }



    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
