package com.panpan.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panpan.common.utils.PageUtils;
import com.panpan.gulimall.coupon.entity.HomeSubjectSpuEntity;

import java.util.Map;

/**
 * δΈι’εε
 *
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 18:18:06
 */
public interface HomeSubjectSpuService extends IService<HomeSubjectSpuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

