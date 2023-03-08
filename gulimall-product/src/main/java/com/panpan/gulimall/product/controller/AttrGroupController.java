package com.panpan.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.panpan.gulimall.product.entity.AttrEntity;
import com.panpan.gulimall.product.service.AttrAttrgroupRelationService;
import com.panpan.gulimall.product.service.AttrService;
import com.panpan.gulimall.product.service.CategoryService;
import com.panpan.gulimall.product.vo.AttrAttrgroupRelationVo;
import com.panpan.gulimall.product.vo.AttrgroupsWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.panpan.gulimall.product.entity.AttrGroupEntity;
import com.panpan.gulimall.product.service.AttrGroupService;
import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.R;



/**
 * 属性分组
 *
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 15:34:39
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private AttrAttrgroupRelationService relationService;

    @RequestMapping("/list/{catelogId}")
    public R listOne(@RequestParam Map<String, Object> params,@PathVariable(value = "catelogId")Long catelogId){
        PageUtils page = attrGroupService.queryPage(params,catelogId);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
		Long[] catelogPath=categoryService.getCatelogPath(attrGroup.getCatelogId());
        attrGroup.setCatelogPath(catelogPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));
        return R.ok();
    }
//    查询组（attrgroupid）关联的所有属性（attr）
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId")long attrGroupId){
        List<AttrEntity> attrEntities=attrService.getRelationAttr(attrGroupId);
        return R.ok().put("data", attrEntities);
    }
//  删除多条对应的attrgroupid和attrid关系
    @RequestMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrAttrgroupRelationVo[] vos ){
        attrGroupService.removeRelation(vos);
        return R.ok();
    }
//    新增relation
    @RequestMapping("/attr/relation")
    public R saveRelation(@RequestBody AttrAttrgroupRelationVo[] relationVos){
        relationService.saveBatch(relationVos);
        return R.ok();
    }

//    查询当前组(attrgroupId)未关联的基本属性（attrId，attr_type=1）
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R surplusRelation(@PathVariable("attrgroupId") long attrGroupId,@RequestParam Map<String,Object> params){
        PageUtils page=attrService.surplusRelation(attrGroupId,params);
        return R.ok().put("page",page);
    }
    @GetMapping("/{catelogId}/withattr")
    public R attrgroupsWithAttrsByCatelogId(@PathVariable("catelogId")Long catelogId){
        List<AttrgroupsWithAttrsVo> vos=attrGroupService.attrgroupsWithAttrsByCatelogId(catelogId);
        return R.ok().put("data",vos);
    }
    @GetMapping("/{catelogId}/withonlybaseattr")
    public R AttrgroupsWithAttrsByCatelogId(@PathVariable("catelogId")Long catelogId){
        List<AttrgroupsWithAttrsVo> vos=attrGroupService.attrgroupsWithAttrsInOneByCatelogId(catelogId);
        return R.ok().put("data",vos);
    }

}
