package com.papan.gulimall.auth_server.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author panpan
 * @create 2021-09-08 上午11:47
 */
@ToString
@Data
public class LoggingVo {
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    private String mobile;
}
