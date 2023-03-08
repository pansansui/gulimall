package com.panpan.gulimall.member.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.panpan.common.exceptionenum.ExceptionNumber;
import com.panpan.common.utils.R;
import com.panpan.gulimall.member.entity.MemberEntity;
import com.panpan.gulimall.member.service.MemberService;
import com.panpan.gulimall.member.to.LoggingTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author panpan
 * @create 2021-09-08 下午12:54
 */
@RestController
public class LoggingController {
    @Autowired
    MemberService memberService;
    @PostMapping("logging")
    public R logging(@RequestBody LoggingTo loggingTo){
        List<MemberEntity> list = memberService.list(new QueryWrapper<MemberEntity>().eq("username", loggingTo.getUsername()).or().eq("mobile", loggingTo.getUsername()));
        if (list==null||list.size()<1){
            return R.error(ExceptionNumber.UNREGISTERED_EXCEPTION.getCode(),ExceptionNumber.UNREGISTERED_EXCEPTION.getMessage());
        }else {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if(!encoder.matches(loggingTo.getPassword(),list.get(0).getPassword())){
                return R.error(ExceptionNumber.LOGGING_WRONG_PASSWORD.getCode(), ExceptionNumber.LOGGING_WRONG_PASSWORD.getMessage());
            }
        }
        return R.ok();
    }

}
