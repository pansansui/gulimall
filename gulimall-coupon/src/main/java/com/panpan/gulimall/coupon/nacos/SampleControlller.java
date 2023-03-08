package com.panpan.gulimall.coupon.nacos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author panpan
 * @create 2021-05-23 下午9:42
 */
@RefreshScope
public class SampleControlller {

    @Value("usr.name")
    String userName;
    @Value("usr.age")
    String usrAge;

}
