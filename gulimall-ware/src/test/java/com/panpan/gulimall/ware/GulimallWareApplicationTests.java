package com.panpan.gulimall.ware;

import com.panpan.gulimall.ware.service.PurchaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GulimallWareApplicationTests {
    @Autowired
    private PurchaseService purchaseService;

    @Test
    void contextLoads() {
    }

}
