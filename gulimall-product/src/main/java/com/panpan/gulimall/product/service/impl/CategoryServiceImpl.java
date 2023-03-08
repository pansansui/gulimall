package com.panpan.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.Query;
import com.panpan.gulimall.product.dao.CategoryDao;
import com.panpan.gulimall.product.entity.CategoryEntity;
import com.panpan.gulimall.product.service.CategoryBrandRelationService;
import com.panpan.gulimall.product.service.CategoryService;
import com.panpan.gulimall.product.vo.thymeleafvo.Catalog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Autowired
    private CategoryBrandRelationService relationService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> entities = baseMapper.selectList(null);
        List<CategoryEntity> results = entities.stream()
                .filter(entity -> entity.getParentCid().equals(0l))
                .map((entity) -> {
                    entity.setChildren(getChildren(entity, entities));
                    return entity;
                })
                .sorted((entity1, entity2) -> entity1.getSort() - entity2.getSort())
                .collect(Collectors.toList());

        return results;
    }

    @Override
    public void removeIfNotConnectionByIds(List<Long> catIds) {
        // TODO 事先判断一下是否有关联，没有才删除

        // 删除
        baseMapper.deleteBatchIds(catIds);

    }
    /*
    * @Description: catelogId/catId
    * @Param:
    * @return: root到catId的路径Long[]
    * @Author: panpan
    * @Date:3/6/2021
    */
    @Override
    public Long[] getCatelogPath(Long catelogId) {
        ArrayList<Long> longs = new ArrayList<>();
        getCatIdPath(longs,catelogId);
        Collections.reverse(longs);
        return longs.toArray(new Long[longs.size()]);
    }

    @Override
    public void updatedetailsById(CategoryEntity category) {
        if (!StringUtils.isEmpty(category.getName())){
            relationService.updateCategory(category.getCatId(),category.getName());
        };
        baseMapper.updateById(category);
    }
    /*
    * @Description: 返回所有一级分类
    * @ParamType:[]
    * @Param: null
    * @return: CategoryEntity>
    * @Author: panpan
    * @Date:29/8/2021
    */
    @Cacheable(value = "gulimall",key = "#root.methodName",sync = true)
    @Override
    public List<CategoryEntity> getCategorysForLevel1() {
        List<CategoryEntity> level = this.list(new QueryWrapper<CategoryEntity>().eq("cat_level", 1l));
        return level;
    }
/*
    //outofdirectmemory 默认redis用lettuce 使用jedis
    @Override
    public List<CategoryEntity> getCategorysForLevel1OnRedis() {
        String lv1Categorys = redisTemplate.opsForValue().get("lv1Categorys");
        if(StringUtils.isEmpty(lv1Categorys)){
            List<CategoryEntity> level = this.getCategorysForLevel1();
            lv1Categorys=JSONObject.toJSONString(level);
            redisTemplate.opsForValue().set("lv1Categorys",lv1Categorys);
            return level;
        }
        return JSONObject.parseObject(lv1Categorys,new TypeReference<List<CategoryEntity>>(){});
    }

    @Override
    public Map<String, List<Catalog2Vo>> getCategorysGTLevel1OnRedis() {
        String lv1Categorys = redisTemplate.opsForValue().get("gtLv1Categorys");
        if(StringUtils.isEmpty(lv1Categorys)){
            Map<String, List<Catalog2Vo>> gtLevel1 = this.getCategorysGTLevel1();
            lv1Categorys=JSONObject.toJSONString(gtLevel1);
            redisTemplate.opsForValue().set("gtLv1Categorys",lv1Categorys);
            return gtLevel1;
        }
        return JSONObject.parseObject(lv1Categorys,new TypeReference<Map<String, List<Catalog2Vo>>>(){});
    }
*/



    /*
    * @Description: 返回大于1级的菜单
    * @ParamType:[]
    * @Param: null
    * @return: Map<String, List<Catalog2Vo>>
    * @Author: panpan
    * @Date:29/8/2021
    */
    @Override
    public Map<String, List<Catalog2Vo>> getCategorysGTLevel1() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        Map<String, List<Catalog2Vo>> result = new HashMap<String, List<Catalog2Vo>>();
        // 一级
        categoryEntities.stream().filter(item->item.getCatLevel()==1).forEach((item) -> {
            //对应parentId --2级
            List<Catalog2Vo> catalog2VoList = categoryListParentIdFilter(categoryEntities, item.getCatId()).map((lv2) -> {
                Catalog2Vo catalog2Vo = new Catalog2Vo();
                catalog2Vo.setName(lv2.getName());
                catalog2Vo.setCatalog1Id(lv2.getParentCid().toString());
                catalog2Vo.setId(lv2.getCatId().toString());
                //对应parentId --3级
                List<Catalog2Vo.Catalog3Vo> catalog3VoList = categoryListParentIdFilter(categoryEntities, lv2.getCatId()).map((lv3) -> {
                    Catalog2Vo.Catalog3Vo catalog3Vo = new Catalog2Vo.Catalog3Vo();
                    catalog3Vo.setCatalog2Id(lv3.getParentCid().toString());
                    catalog3Vo.setId(lv3.getCatId().toString());
                    catalog3Vo.setName(lv3.getName());
                    return catalog3Vo;
                }).collect(Collectors.toList());
                catalog2Vo.setCatalog3List(catalog3VoList);
                return catalog2Vo;
            }).collect(Collectors.toList());
            result.put(item.getCatId().toString(),catalog2VoList);
        });
        // TODO: 29/8/2021 优化
        return result;
    }
    /*
    * @Description:
    * @ParamType:[java.util.List<com.panpan.gulimall.product.entity.CategoryEntity>, java.lang.Integer]
    * @Param: categoryEntities,level
    * @return: CategoryEntity>
    * @Author: panpan
    * @Date:29/8/2021
    */
    public Stream<CategoryEntity> categoryListParentIdFilter(List<CategoryEntity> categoryEntities, Long parentId){
        return categoryEntities.stream().filter(categoryEntity -> categoryEntity.getParentCid()==parentId);
    }

    // 递归获得路径catelogPath
    public void getCatIdPath(ArrayList<Long> arrayList, Long catelogId) {
        CategoryEntity categoryEntity = baseMapper.selectById(catelogId);
        arrayList.add(catelogId);
        if(categoryEntity.getParentCid()==null||categoryEntity.getParentCid()==0){

        }else {
            getCatIdPath(arrayList,categoryEntity.getParentCid());
        }
    }


    private List<CategoryEntity> getChildren(CategoryEntity parent,List<CategoryEntity> all){
        List<CategoryEntity> children = all.stream()
                .filter(entity -> entity.getParentCid().equals(parent.getCatId()))
                .map(entity -> {
                    // 递归找子菜单
                    entity.setChildren(getChildren(entity, all));
                    return entity;
                })
                // 排序
                .sorted((entity1, entity2) -> entity1.getSort() - entity2.getSort())
                .collect(Collectors.toList());
        return children;

    }

}