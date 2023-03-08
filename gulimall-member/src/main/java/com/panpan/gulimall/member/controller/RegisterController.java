package com.panpan.gulimall.member.controller;

import com.panpan.common.to.gulimallmember.RegisterInfoTo;
import com.panpan.common.utils.R;
import com.panpan.gulimall.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author panpan
 * @create 2021-09-07 下午9:19
 */
@RestController
@RequestMapping("register")
public class RegisterController {
    @Autowired
    MemberService memberService;
    @RequestMapping("/save")
    public R saveRegisterInfo(@RequestBody RegisterInfoTo registerInfoTo){
        System.out.println(registerInfoTo);
        memberService.save(registerInfoTo);
        return R.ok();
    }

}
