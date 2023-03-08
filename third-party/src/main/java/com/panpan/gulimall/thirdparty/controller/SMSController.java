package com.panpan.gulimall.thirdparty.controller;

import com.panpan.common.utils.R;
import com.panpan.gulimall.thirdparty.SMS.MySMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * @author panpan
 * @create 2021-09-07 下午12:03
 */
@Controller
@RequestMapping("thirdparty")
public class SMSController {
    @Autowired
    MySMS mySMS;
    @RequestMapping("/sms/send")
    @ResponseBody
    public R sendSMS(@RequestBody String phoneNum){
        mySMS.sendSMS(phoneNum,UUID.randomUUID().toString().substring(3, 8));
        return R.ok();
    }
}
