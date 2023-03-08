package com.panpan.gulimall.product;

import com.panpan.gulimall.product.service.SkuSaleAttrValueService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
class GulimallProductApplicationTests {
    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @Test
    void contextLoads() throws InterruptedException, IOException, NoSuchFieldException, IllegalAccessException {
        System.out.println(threadPoolExecutor.getActiveCount());
        System.out.println(threadPoolExecutor.getCorePoolSize());
        System.out.println(threadPoolExecutor.getKeepAliveTime(TimeUnit.SECONDS));
        Field allowCoreThreadTimeOut = threadPoolExecutor.getClass().getDeclaredField("allowCoreThreadTimeOut");
        allowCoreThreadTimeOut.setAccessible(true);
        System.out.println(allowCoreThreadTimeOut.get(threadPoolExecutor));

    }
}
