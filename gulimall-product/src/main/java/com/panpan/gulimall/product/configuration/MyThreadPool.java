package com.panpan.gulimall.product.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author panpan
 * @create 2021-09-05 下午5:44
 */
@Configuration
@ConditionalOnClass(MyThreadPoolProperties.class)
public class MyThreadPool {

    @Bean(name = "executor")
    public ThreadPoolExecutor threadPoolExecutor(MyThreadPoolProperties myThreadPoolProperties){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(myThreadPoolProperties.getCoreSize(), myThreadPoolProperties.getMaxSize(), myThreadPoolProperties.getKeepAlive(), TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(10000), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        return threadPoolExecutor;

    }
}
