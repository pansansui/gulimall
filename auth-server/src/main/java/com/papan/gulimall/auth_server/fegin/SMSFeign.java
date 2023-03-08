package com.papan.gulimall.auth_server.fegin;

import com.panpan.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author panpan
 * @create 2021-09-07 下午1:07
 */
@FeignClient("third-party")
public interface SMSFeign {
    @RequestMapping("thirdparty/sms/send")
    public R sendSMS(String phoneNum);
}
