package com.panpan.gulimall.product.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author panpan
 * @create 2021-09-05 下午5:53
 */
@ConfigurationProperties(prefix = "gulimall.threadpool")
@Component
@Data
public class MyThreadPoolProperties {
    @Value("12")
    private Integer coreSize;
    @Value("36")
    private Integer maxSize;
    @Value("10")
    private Integer keepAlive;
}
