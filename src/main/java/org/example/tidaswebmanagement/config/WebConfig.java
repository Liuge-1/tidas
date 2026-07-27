package org.example.tidaswebmanagement.config;

import org.example.tidaswebmanagement.constant.BusinessConstants;
import org.example.tidaswebmanagement.interceptor.RateLimitInterceptor;
import org.example.tidaswebmanagement.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RateLimitInterceptor rateLimitInterceptor;

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 限流拦截器：优先级更高，只对写操作生效
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/**")
                .order(0);

        // Token 鉴权拦截器
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(BusinessConstants.EXCLUDE_PATHS)
                .order(1);
    }
}