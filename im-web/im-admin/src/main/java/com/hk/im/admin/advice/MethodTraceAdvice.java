package com.hk.im.admin.advice;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author : HK意境
 * @ClassName : MethodTraceAdvice
 * @date : 2023/3/2 10:13
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Aspect
@Configuration
public class MethodTraceAdvice {

    @Pointcut("cutController() || cutService()")
    public void monitor() {

    }

    @Pointcut("execution(* com.hk.im..*.service.*(..))")
    public void cutController() {}

    @Pointcut("execution(* com.hk.im..*.controller.*(..))")
    public void cutService() {

    }


    /**
     * 方法入参，返回值追踪
     *
     */
    @Bean
    public Advisor methodTraceAdvisor() {
        CustomizableTraceInterceptor traceInterceptor = new CustomizableTraceInterceptor();
        traceInterceptor.setEnterMessage("Enter Class=$[targetClassName]: $[methodName]($[argumentTypes] $[arguments])");
        traceInterceptor.setExitMessage("Exit Class=$[targetClassName]: $[methodName]()->$[returnValue]");
        traceInterceptor.setUseDynamicLogger(true);

        // 添加切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("com.hk.im.admin.advice.MethodTraceAdvice.monitor()");

        // 返回切面
        return new DefaultPointcutAdvisor(pointcut, traceInterceptor);
    }


}
