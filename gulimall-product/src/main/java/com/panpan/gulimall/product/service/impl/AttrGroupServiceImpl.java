package com.panpan.gulimall.product.service.impl;

import com.panpan.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.panpan.gulimall.product.entity.AttrEntity;
import com.panpan.gulimall.product.service.AttrService;
import com.panpan.gulimall.product.vo.AttrAttrgroupRelationVo;
import com.panpan.gulimall.product.vo.AttrgroupsWithAttrsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.Query;

import com.panpan.gulimall.product.dao.AttrGroupDao;
import com.panpan.gulimall.product.entity.AttrGroupEntity;
import com.panpan.gulimall.product.service.AttrGroupService;
import org.springframework.util.StringUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrAttrgroupRelationDao relationDao;
    @Autowired
    private AttrService attrService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }


/*
* @Description: 模糊查询
* @Param:
* @return:
* @Author: panpan
* @Date:3/6/2021
*/
    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
        if (catelogId == 0 || catelogId == null) {

        } else {
            wrapper.eq("catelog_Id", catelogId);
        }
        String key = (String)params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and((obj)->obj.eq("attr_group_id",key).or().like("attr_group_name",key));
        }
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                wrapper);
        return new PageUtils(page);
    }

    @Override
    public void removeRelation(AttrAttrgroupRelationVo[] vos) {
        // 批量删除关系
        relationDao.removeBatchRelaton(vos);
    }
/*
* @Description: 根据catelogId 查attrgroup表，查attrattrgrouprelation表，attr表 返回代attrs的attrgroup
* @Param: [catelogId]
* @return: java.util.List<com.panpan.gulimall.product.vo.AttrgroupsWithAttrsVo>
* @Author: panpan
* @Date:10/8/2021
*/
    @Override
    public List<AttrgroupsWithAttrsVo> attrgroupsWithAttrsByCatelogId(Long catelogId) {
        List<AttrGroupEntity> groupEntities = baseMapper.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_Id", catelogId));
        List<AttrgroupsWithAttrsVo> vos = groupEntities.stream().map((item) -> {
            AttrgroupsWithAttrsVo attrgroupsWithAttrsVo = new AttrgroupsWithAttrsVo();
            BeanUtils.copyProperties(item, attrgroupsWithAttrsVo);
            List<AttrEntity> relationAttr = attrService.getRelationAttr(item.getAttrGroupId());
            attrgroupsWithAttrsVo.setAttrs(relationAttr);
            return attrgroupsWithAttrsVo;
        }).collect(Collectors.toList());
        return vos;
    }

    @Override
    public List<AttrgroupsWithAttrsVo> attrgroupsWithAttrsInOneByCatelogId(Long catelogId) {
        List<AttrGroupEntity> groupEntities = baseMapper.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_Id", catelogId));
        List<AttrgroupsWithAttrsVo> vos = groupEntities.stream().map((item) -> {
            AttrgroupsWithAttrsVo attrgroupsWithAttrsVo = new AttrgroupsWithAttrsVo();
            BeanUtils.copyProperties(item, attrgroupsWithAttrsVo);
            List<AttrEntity> relationAttr = attrService.getRelationAttrInOne(item.getAttrGroupId());
            attrgroupsWithAttrsVo.setAttrs(relationAttr);
            return attrgroupsWithAttrsVo;
        }).collect(Collectors.toList());
        return vos;
    }

}