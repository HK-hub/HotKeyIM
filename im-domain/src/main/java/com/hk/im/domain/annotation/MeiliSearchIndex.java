package com.hk.im.domain.annotation;

import java.lang.annotation.*;

/**
 * @author : HK意境
 * @ClassName : MeiliSearchIndex
 * @date : 2023/4/16 14:31
 * @description : 标注在实体类上，进行标识index 索引
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MeiliSearchIndex {

    /**
     * 索引名称：默认为注解标识的类名
     * @return
     */
    String index() default "";

    String primaryKey() default "";

}
