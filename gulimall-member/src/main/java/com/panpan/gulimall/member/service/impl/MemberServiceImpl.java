package com.panpan.gulimall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panpan.common.to.gulimallmember.RegisterInfoTo;
import com.panpan.common.utils.PageUtils;
import com.panpan.common.utils.Query;
import com.panpan.gulimall.member.dao.MemberDao;
import com.panpan.gulimall.member.entity.MemberEntity;
import com.panpan.gulimall.member.service.MemberService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {
    @Autowired
    RedissonClient redissonClient;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void save(RegisterInfoTo registerInfoTo) {
        RLock lock = redissonClient.getLock("member:save:" + registerInfoTo.getUsername());
        long defaultLevel=baseMapper.getDefaultId();
        String password = new BCryptPasswordEncoder().encode(registerInfoTo.getPassword());
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setUsername(registerInfoTo.getUsername());
        memberEntity.setPassword(password);
        memberEntity.setMobile(registerInfoTo.getPhoneNum());
        memberEntity.setNickname(registerInfoTo.getUsername());
        memberEntity.setCreateTime(new Date());
        memberEntity.setLevelId(defaultLevel);
        try {lock.lock(30l, TimeUnit.SECONDS);
            checkUserName(registerInfoTo.getUsername());
            checkPhoneNum(registerInfoTo.getPhoneNum());
            this.save(memberEntity);
        } finally {
            lock.unlock();
        }

    }
    private void checkPhoneNum(String phoneNum) {
        Integer count = baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phoneNum));
        if (count>0){
            throw new RuntimeException("手机已被占用");
        }
    }

    private void checkUserName(String username) {
        Integer count = baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("username", username));
        if (count>0){
            throw new RuntimeException("用户名已被占用");
        }
    }

}