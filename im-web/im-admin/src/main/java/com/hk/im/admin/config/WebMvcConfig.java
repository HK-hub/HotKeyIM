package com.hk.im.admin.config;

import com.hk.im.admin.interceptor.UserTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserTokenInterceptor(this.stringRedisTemplate))
                // .addPathPatterns("/user/**")
                .addPathPatterns("/info/**")
                // 发现好友
                //.addPathPatterns("")
                //开放登录, 注册，忘记密码, 验证码, 生产用户
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/logout")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/user/forget")
                .excludePathPatterns("/user/code")
                .excludePathPatterns("/user/generate")

                // 业务测试
                .excludePathPatterns("/user/**")

        ;

    }


    static final String ORIGINS[] = new String[]{"GET", "POST", "PUT", "DELETE", "TRANCE", "HEAD", "OPTION"};

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods(ORIGINS)
                .maxAge(3600);
    }

}
