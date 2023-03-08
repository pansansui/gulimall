package com.panpan.gulimall.product.configuration;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

/**
 * @author panpan
 * @create 2021-09-01 上午11:16
 */
@EnableConfigurationProperties(CacheProperties.class)
@Configuration
@EnableCaching
public class MyRedisCacheingConfig {
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties){
        RedisCacheConfiguration redisCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfig= redisCacheConfig.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()));
        redisCacheConfig=redisCacheConfig.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()));
        redisCacheConfig=redisCacheConfig.entryTtl(cacheProperties.getRedis().getTimeToLive()==null? Duration.ZERO:cacheProperties.getRedis().getTimeToLive());redisCacheConfig=redisCacheConfig.entryTtl(cacheProperties.getRedis().getTimeToLive()==null? Duration.ZERO:cacheProperties.getRedis().getTimeToLive());
        if(cacheProperties.getRedis().getKeyPrefix()!=null) {
            redisCacheConfig = redisCacheConfig.prefixCacheNameWith(cacheProperties.getRedis().getKeyPrefix());
        }
        if(!cacheProperties.getRedis().isCacheNullValues()){
            redisCacheConfig=redisCacheConfig.disableCachingNullValues();
        }
        return redisCacheConfig;
    }
}
