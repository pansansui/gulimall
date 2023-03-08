package com.panpan.gulimall.member.to;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author panpan
 * @create 2021-09-08 下午12:56
 */
@Data
public class LoggingTo {
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
}

