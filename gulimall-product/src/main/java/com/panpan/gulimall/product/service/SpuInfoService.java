package com.panpan.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panpan.common.utils.PageUtils;
import com.panpan.gulimall.product.entity.SpuInfoEntity;
import com.panpan.gulimall.product.vo.SaveSPUVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 15:34:39
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void save(SaveSPUVo spuVo);

    PageUtils queryPageByCondition(Map<String, Object> params);

    void putSpuById(Long spuId);
}

