package com.panpan.gulimall.coupon;


import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GulimallCouponApplicationTests {
    @Value("${spring.cloud.alicloud.oss.endpoint}")
    String string;

    @Test
    void contextLoads() {

        System.out.println("+++++++++++++++++++");
        System.out.println(string);
    }

}
