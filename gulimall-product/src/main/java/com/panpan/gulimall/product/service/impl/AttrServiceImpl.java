package com.panpan.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.Query;
import com.panpan.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.panpan.gulimall.product.dao.AttrDao;
import com.panpan.gulimall.product.dao.AttrGroupDao;
import com.panpan.gulimall.product.dao.CategoryDao;
import com.panpan.gulimall.product.entity.*;
import com.panpan.gulimall.product.service.AttrService;
import com.panpan.gulimall.product.service.CategoryService;
import com.panpan.gulimall.product.service.ProductAttrValueService;
import com.panpan.gulimall.product.vo.AttrRespVo;
import com.panpan.gulimall.product.vo.AttrVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    private AttrAttrgroupRelationDao relationDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private AttrGroupDao attrGroupDao;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveVo(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        baseMapper.insert(attrEntity);
        if(attr.getAttrGroupId()!=null&&attr.getAttrGroupId()!=0l){
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            AttrEntity entity = baseMapper.selectOne(new QueryWrapper<AttrEntity>().eq("attr_name", attrEntity.getAttrName()).eq("catelog_id", attrEntity.getCatelogId()));
            attrAttrgroupRelationEntity.setAttrId(entity.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationDao.insert(attrAttrgroupRelationEntity);
        }
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, long catelogId, String attrType) {
        QueryWrapper<AttrEntity> wrapper=new QueryWrapper<AttrEntity>().in("attr_type","base".equalsIgnoreCase(attrType)? 1:0,2);
        if(!StringUtils.isEmpty(params.get("key"))){
            wrapper.like("attr_name ",params.get("key")).or().like("value_select",params.get("key"));
        }
        if(catelogId!=0l){
            wrapper.and(item->item.eq("catelog_id",catelogId));
        }
        // ????????????
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();

        // ????????????????????????
        List<AttrRespVo> respVos = records.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            // ?????????Vo
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrRespVo.getAttrId()));
            // ??????????????????????????????
            if (attrAttrgroupRelationEntity != null) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelationEntity.getAttrGroupId());
                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
            ;
            CategoryEntity categoryEntity = categoryDao.selectOne(new QueryWrapper<CategoryEntity>().eq("cat_id", attrRespVo.getCatelogId()));
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            ;
            return attrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(respVos);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo attrRespVo=new AttrRespVo();
        // ID?????????
        AttrEntity byId = this.getById(attrId);
        // ??????????????????
        BeanUtils.copyProperties(byId,attrRespVo);
//        ??????????????????id?????????
        AttrAttrgroupRelationEntity attrGroupRelation = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
        if(attrGroupRelation!=null){
            attrRespVo.setAttrGroupId(attrGroupRelation.getAttrGroupId());
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrRespVo.getAttrGroupId());
            if (attrGroupEntity!=null){
                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
        }
//        ??????????????????
        Long catelogId = attrRespVo.getCatelogId();
        Long[] catelogPath = categoryService.getCatelogPath(catelogId);
        if(catelogPath!=null) {
            attrRespVo.setCatelogPath(catelogPath);
        }
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if(categoryEntity!=null){
            attrRespVo.setCatelogName(categoryEntity.getName());
        }
        return attrRespVo;

    }
    @Transactional
    @Override
    public void updateAttr(AttrVo attr) {
        // ??????attr???
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        this.updateById(attrEntity);
        // ???????????????  ????????????????????? ????????? ?????????
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        BeanUtils.copyProperties(attr,relationEntity);
        Integer count = relationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
//        ??????????????????????????????,????????????1???????????????0
        if (attr.getAttrType()==1){
            if(count>0){
                relationDao.update(relationEntity,new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attr.getAttrId()));
            }else{
                relationDao.insert(relationEntity);
            }
        }
    }
//    ????????????id?????????????????????attr??????
    @Override
    public List<AttrEntity> getRelationAttr(long attrGroupId) {
        List<AttrAttrgroupRelationEntity> relations= relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId));
        List<Long> attrIds = relations.stream().map((entity) -> {
            return entity.getAttrId();
        }).collect(Collectors.toList());
        List<AttrEntity> attrEntities=new ArrayList<>();

        if(!attrIds.isEmpty()){
            attrEntities = this.listByIds(attrIds);
        }
        return attrEntities;
    }

    @Override
    public PageUtils surplusRelation(long attrGroupId, Map<String, Object> params) {
        //        ?????????????????????catelog?????????
        AttrGroupEntity groupEntity = attrGroupDao.selectById(attrGroupId);
        Long catelogId=groupEntity.getCatelogId();
        // ????????????catelog?????????????????????attrgroup
        List<AttrGroupEntity> groupEntities = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<Long> groups = groupEntities.stream().map((item) -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());
//        ??????catelog????????????????????????attrid
        List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id",groups));
        List<Long> attrs = relationEntities.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());
//        ??????catelog????????????????????????attrid
        QueryWrapper<AttrEntity> attrQueryWrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).eq("show_desc", 1);
        if(!attrs.isEmpty()&&attrs.size()>0){
            attrQueryWrapper.notIn("attr_id", attrs);
        }
        String key = (String)params.get("key");
        if(!StringUtils.isEmpty(key)){
            attrQueryWrapper.and((item) -> {item.eq("catelog_id",key).or().like("attr_name",key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), attrQueryWrapper);
        PageUtils pageUtils = new PageUtils(page);

        return pageUtils;


    }

    @Override
    public List<AttrEntity> getRelationAttrInOne(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> relations= relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId));
        List<Long> attrIds = relations.stream().map((entity) -> {
            return entity.getAttrId();
        }).collect(Collectors.toList());
        List<AttrEntity> attrEntities=new ArrayList<>();

        if(!attrIds.isEmpty()){
            attrEntities = this.list(new QueryWrapper<AttrEntity>().in("attr_id",attrIds).eq("attr_type",1));
        }

        return attrEntities;
    }

    @Override
    public List<AttrEntity> getRelationSaleAttr(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> relations= relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId));
        List<Long> attrIds = relations.stream().map((entity) -> {
            return entity.getAttrId();
        }).collect(Collectors.toList());
        List<AttrEntity> attrEntities=new ArrayList<>();

        if(!attrIds.isEmpty()){
            attrEntities = this.listByIds(attrIds);
        }
        return attrEntities;
    }

    @Override
    public List<AttrEntity> getRelationbaseAttr(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> relations= relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId));
        List<Long> attrIds = relations.stream().map((entity) -> {
            return entity.getAttrId();
        }).collect(Collectors.toList());
        List<AttrEntity> attrEntities=new ArrayList<>();

        if(!attrIds.isEmpty()){
            attrEntities = this.listByIds(attrIds);
        }
        return attrEntities;
    }

    @Override
    public List<ProductAttrValueEntity> listForSpu(Long spuId) {
        List<ProductAttrValueEntity> productAttrValueEntities = productAttrValueService.list(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        return productAttrValueEntities;
    }
   /*
   * @Description: ???????????????????????????
   * @ParamType:[java.lang.Long, java.util.List<com.panpan.gulimall.product.entity.ProductAttrValueEntity>]
   * @Param: spuId,productAttrValueEntities
   * @return: null
   * @Author: panpan
   * @Date:27/8/2021
   */
    @Transactional
    @Override
    public void saveBatchBySpuId(Long spuId, List<ProductAttrValueEntity> productAttrValueEntities) {
        productAttrValueService.remove(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id",spuId));
        productAttrValueEntities.stream().forEach((item) -> {
            item.setSpuId(spuId);
        });
        productAttrValueService.saveBatch(productAttrValueEntities);

    }

    @Override
    public List<Long> listSearchableByIds(List<Long> attrIdList) {
        return baseMapper.listSearchableByIds(attrIdList);
    }

}