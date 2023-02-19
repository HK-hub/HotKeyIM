package com.hk.im.admin.config;

import com.hk.im.admin.interceptor.UserLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userLoginInterceptor())
                .addPathPatterns("/**")
                //开放登录, 注册，忘记密码, 验证码, 生产用户
                .excludePathPatterns("/**/login")
                .excludePathPatterns("/**/register")
                .excludePathPatterns("/**/forget")
                .excludePathPatterns("/**/code")
                .excludePathPatterns("/**/generate");

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


    @Bean
    public UserLoginInterceptor userLoginInterceptor() {
        return new UserLoginInterceptor();
    }

}
