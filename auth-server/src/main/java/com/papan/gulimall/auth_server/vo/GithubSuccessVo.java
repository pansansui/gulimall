package com.papan.gulimall.auth_server.vo;

import lombok.Data;

/**
 * @author panpan
 * @create 2021-09-08 下午6:45
 */
@Data
public class GithubSuccessVo {

    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String refresh_token_expires_in;
    private String scope;
    private String token_type;
}
