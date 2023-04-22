package com.hk.im.admin.config;

import com.hk.im.admin.interceptor.LogInterceptor;
import com.hk.im.admin.interceptor.UserLoginInterceptor;
import com.hk.im.admin.resolver.CustomMethodArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // log 拦截器
        registry.addInterceptor(logInterceptor())
                .addPathPatterns("/**");
        // token 拦截器
    }



    // 跨域支持方法
    static final String ORIGINS[] = {"GET", "POST", "PUT", "DELETE", "TRANCE", "HEAD", "OPTIONS"};

    /**
     * 跨域配置
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods(ORIGINS)
                .maxAge(3600);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CustomMethodArgumentResolver());
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }

    // @Bean
    public UserLoginInterceptor userLoginInterceptor() {
        return new UserLoginInterceptor();
    }


    @Bean
    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

}
