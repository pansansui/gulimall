package com.panpan.gulimall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * @author panpan
 * @create 2021-05-26 下午1:05
 */
@Configuration
public class CorsConfigDIY {
    /*
    * @Description:为跨域设置头信息
    * @Param:
    * @return:
    * @Author: panpan
    * @Date:26/5/2021
    */
    @Bean
    public CorsWebFilter corsWebFilter(){
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 所有请求头
        corsConfiguration.addAllowedHeader("*");
        // 所有方式get，post，option
        corsConfiguration.addAllowedMethod("*");
        // 所有源
        corsConfiguration.addAllowedOrigin("*");
        // 带cookie
        corsConfiguration.setAllowCredentials(true);
        configurationSource.registerCorsConfiguration("/**",corsConfiguration);

        return new CorsWebFilter(configurationSource);
    }

}
