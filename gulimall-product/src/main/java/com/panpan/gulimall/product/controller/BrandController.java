package com.panpan.gulimall.product.controller;

import java.util.Arrays;

import java.util.Map;

import com.panpan.common.validgroup.GreateGroup;
import com.panpan.gulimall.product.groups.GGreate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.panpan.gulimall.product.entity.BrandEntity;
import com.panpan.gulimall.product.service.BrandService;
import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.R;




/**
 * 品牌
 *
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 15:34:39
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@Validated(GreateGroup.class) @RequestBody BrandEntity brand  /*,BindingResult bindingResult*/){
        /*if (bindingResult.hasErrors()){
            HashMap<String, String> map = new HashMap<>();
            bindingResult.getFieldErrors().forEach((message)->{
                String defaultMessage = message.getDefaultMessage();
                String field = message.getField();
                map.put(field,defaultMessage);
            });
            return R.error("错误").put("data",map);
        }*/
		brandService.save(brand);

        return R.ok();
    }

    /**
     * 修改
     */
    @Transactional
    @RequestMapping("/update")
    public R update(@RequestBody BrandEntity brand){
		brandService.updateAndRelation(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
