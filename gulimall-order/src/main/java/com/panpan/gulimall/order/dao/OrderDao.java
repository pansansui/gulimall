package com.panpan.gulimall.order.dao;

import com.panpan.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 18:07:24
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
