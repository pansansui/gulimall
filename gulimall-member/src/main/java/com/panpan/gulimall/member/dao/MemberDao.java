package com.panpan.gulimall.member.dao;

import com.panpan.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 18:14:01
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {

    long getDefaultId();
}
