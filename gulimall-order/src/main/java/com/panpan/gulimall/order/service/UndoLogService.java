package com.panpan.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panpan.common.utils.PageUtils;
import com.panpan.gulimall.order.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 18:07:24
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

