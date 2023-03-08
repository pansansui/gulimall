package com.panpan.gulimall.product.controller;

import com.panpan.common.to.SkuWareTo;
import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.R;
import com.panpan.gulimall.product.entity.SkuInfoEntity;
import com.panpan.gulimall.product.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * sku信息
 *
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 15:34:39
 */
@RestController
@RequestMapping("product/skuinfo")
public class SkuInfoController {
    @Autowired
    private SkuInfoService skuInfoService;


    @RequestMapping("/getskuto")
    public SkuWareTo getById(@RequestBody Long skuId){
        System.out.println(skuId+"------------------------------------------------");
        SkuInfoEntity skuInfoEntity = skuInfoService.getById(skuId);
        SkuWareTo skuWareTo = new SkuWareTo();
        skuWareTo.setSkuId(skuInfoEntity.getSkuId());
        skuWareTo.setSkuName(skuInfoEntity.getSkuName());
        skuWareTo.setPrice(skuInfoEntity.getPrice());
        return skuWareTo;
    };
    /**
     * 列表 product/skuinfo/getskuto
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = skuInfoService.queryPageByCondition(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId){
		SkuInfoEntity skuInfo = skuInfoService.getById(skuId);

        return R.ok().put("skuInfo", skuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SkuInfoEntity skuInfo){
		skuInfoService.save(skuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SkuInfoEntity skuInfo){
		skuInfoService.updateById(skuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] skuIds){
		skuInfoService.removeByIds(Arrays.asList(skuIds));

        return R.ok();
    }

}
