package com.papan.gulimall.auth_server.fegin;

import com.panpan.common.utils.R;
import com.papan.gulimall.auth_server.vo.LoggingVo;
import com.papan.gulimall.auth_server.vo.RegisterInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author panpan
 * @create 2021-09-07 下午8:54
 */
@FeignClient("gulimall-member")
public interface MemberFeign {
    @RequestMapping("register/save")
    public R saveRegisterInfo(@RequestBody RegisterInfoVo registerInfoVo);
    @PostMapping("logging")
    public R logging(@RequestBody LoggingVo loggingVo);
}
