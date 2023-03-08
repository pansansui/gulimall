package com.panpan.common.to.gulimallmember;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author panpan
 * @create 2021-09-07 下午9:25
 */
@Data
@ToString
public class RegisterInfoTo {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
   @NotBlank(message = "手机号不能为空")
    private String phoneNum;

}
