package com.papan.gulimall.auth_server.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author panpan
 * @create 2021-09-07 下午4:43
 */
@Data
public class RegisterInfoVo {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    @Pattern(regexp = "^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$",message = "非法的手机号")
    private String phoneNum;
    @NotBlank(message = "验证码不能为空")
    private String verificationCode;
}
