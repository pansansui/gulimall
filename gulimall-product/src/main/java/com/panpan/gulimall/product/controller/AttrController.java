package com.panpan.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mysql.cj.log.Log;
import com.panpan.gulimall.product.entity.ProductAttrValueEntity;
import com.panpan.gulimall.product.vo.AttrRespVo;
import com.panpan.gulimall.product.vo.AttrVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.panpan.gulimall.product.entity.AttrEntity;
import com.panpan.gulimall.product.service.AttrService;
import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.R;



/**
 * 商品属性
 *
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 15:34:39
 */
@Slf4j
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);
        return R.ok().put("page", page);
    }
    @RequestMapping("/update/{spuId}")
    public R updateBySpuId(@PathVariable("spuId")Long spuId,@RequestBody List<ProductAttrValueEntity> productAttrValueEntities){
        attrService.saveBatchBySpuId(spuId,productAttrValueEntities);
        return R.ok();
    }


    @RequestMapping("/{attrType}/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,@PathVariable(value = "catelogId") long catelogId,@PathVariable(value = "attrType") String attrType){
        PageUtils page = attrService.queryBaseAttrPage(params,catelogId,attrType);
        return R.ok().put("page", page);
    }
    @RequestMapping("/base/listforspu/{spuId}")
    public R listForSpu(@PathVariable("spuId")Long spuId){
        List<ProductAttrValueEntity> data = attrService.listForSpu(spuId);
        return R.ok().put("data", data);
    }



    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
//		AttrEntity attr = attrService.getById(attrId);
        AttrRespVo attr=attrService.getAttrInfo(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attr){
        attrService.saveVo(attr);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVo attr){
		attrService.updateAttr(attr);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
