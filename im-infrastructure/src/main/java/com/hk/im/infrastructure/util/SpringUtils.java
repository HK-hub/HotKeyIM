package com.hk.im.infrastructure.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * @ClassName : SpringUtils
 * @author : HK意境
 * @date : 2023/1/5 20:14
 * @description : 用于操作 Spring Bean
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Component
public class SpringUtils implements ApplicationContextAware {
 
    private static ApplicationContext applicationContext;
 
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (ObjectUtils.isEmpty(applicationContext)) {
            throw new ApplicationContextException("applicationContext must not be null");
        }
        SpringUtils.applicationContext = applicationContext;
    }
 
 
    /**
     * Gets ApplicationContext
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
 
    /**
     * Gets bean by bean's name
     * @param name    bean's name
     * @return bean
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }
 
 
    /**
     * Gets bean by bean's class(java.lang.Class)
     * @param clazz    bean Class
     * @param <T>      Class type
     * @return bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }
 
 
    /**
     * Gets bean by bean's class and bean's name
     * @param name    bean name
     * @param clazz   bean Class
     * @param <T>     Class type
     * @return bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
 
