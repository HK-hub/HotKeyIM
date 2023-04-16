package com.hk.im.flow.search.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author : HK意境
 * @ClassName : MeiliRepository
 * @date : 2023/4/16 15:44
 * @description : 标注在 MeiliRepository 类上进行 bean 的注册
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface MeiliRepository {

    String value() default "";
}
