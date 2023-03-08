package com.papan.gulimall.auth_server.controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.panpan.common.constant.AuthConst;
import com.panpan.common.utils.R;
import com.papan.gulimall.auth_server.fegin.MemberFeign;
import com.papan.gulimall.auth_server.fegin.SMSFeign;
import com.papan.gulimall.auth_server.vo.RegisterInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author panpan
 * @create 2021-09-07 下午1:11
 */
@Controller
public class RegisterController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    SMSFeign smsFeign;
    @Autowired
    MemberFeign memberFeign;
    @ResponseBody
    @RequestMapping("/sendSMS")
    public R sendSMS(@RequestParam("phoneNum")String phoneNum) throws InterruptedException {
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        String code = AuthConst.SMS_CODE_PREFIX.getValue()+ phoneNum;
        String lock = AuthConst.AUTH_LOCK_PREFIX.getValue() +phoneNum;
        if(StringUtils.isEmpty(stringStringValueOperations.get(code))&&StringUtils.isEmpty(stringStringValueOperations.get(lock))){
            return stringStringValueOperations.setIfAbsent("auth:sms:lock", phoneNum, 10l, TimeUnit.SECONDS)?
            smsFeign.sendSMS(phoneNum):R.error("请勿恶意重试");
        }
        return R.ok();
    }
    @PostMapping ("/submitInfo")
    public String submitInfo(@Valid RegisterInfoVo registerInfoVo, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        Map<String, String> errorMap=new HashMap<>();
        if (bindingResult.hasErrors()){
            errorMap= bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        }else{
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            String verificationCode = ops.get(AuthConst.SMS_CODE_PREFIX.getValue() + registerInfoVo.getPhoneNum());
            // 验证验证码
            if (registerInfoVo.getVerificationCode().equals(verificationCode)){
                stringRedisTemplate.delete(AuthConst.SMS_CODE_PREFIX.getValue() + registerInfoVo.getPhoneNum());
                R r = memberFeign.saveRegisterInfo(registerInfoVo);
                return "redirect:http://auth.gulimall.com/index.html";
            }else {
                errorMap.put("verificationCode","验证码错误");
            }
        }
        redirectAttributes.addFlashAttribute("error",errorMap);
        return "redirect:http://auth.gulimall.com/register.html";
    }
}
