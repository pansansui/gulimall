package com.panpan.gulimall.product.controller;

import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.R;
import com.panpan.gulimall.product.entity.SpuInfoEntity;
import com.panpan.gulimall.product.service.SpuInfoService;
import com.panpan.gulimall.product.vo.SaveSPUVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * spu信息
 *
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 15:34:39
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }

   /* @RequestMapping("/{spuId}/up")
    public R spuUp(@PathVariable("spuId")Long spuId){
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        spuInfoEntity.setId(spuId);
        spuInfoEntity.setPublishStatus(1);
        spuInfoService.updateById(spuInfoEntity);
        return R.ok();
    }*/
    /*
     * @Description:
     * @ParamType:[java.lang.Long]
     * @Param: skuId
     * @return: R
     * @Author: panpan
     * @Date:28/8/2021
     */
    @PostMapping("/{spuId}/up")
    public R putSpuById(@PathVariable("spuId") Long spuId){
       spuInfoService.putSpuById(spuId);
        return R.ok();
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SaveSPUVo spuVo){
		spuInfoService.save(spuVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
