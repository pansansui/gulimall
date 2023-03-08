package com.panpan.gulimall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*配置mybatis-plus
* 1.在common中引入mybatis-plus依赖，mysql驱动
* 2.配置yml数据源信息
* 3.开启mapperScan
* 4.开启mybatis-plus sql映射
* 5.开启主键自增*/


@EnableFeignClients(basePackages = "com.panpan.gulimall.product.feign")
@SpringBootApplication
@EnableDiscoveryClient
public class GulimallProductApplication {


    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
