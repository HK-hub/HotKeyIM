package com.hk.im.domain.annotation;

import java.lang.annotation.*;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisSearchField {

    FieldType fieldType() default FieldType.AUTO;
    String fieldName() default "";
    String desc() default "";
    boolean exist() default true;

}