package com.hk.im.admin.advice;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.ValueConstants;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomParam {

    /**
     * Alias for {@link #name}.
     */
    @AliasFor("name")
    String value() default "";

    /**
     * The name of the request parameter to bind to.
     *
     * @since 4.2
     */
    @AliasFor("value")
    String name() default "";

    /**
     * Whether the parameter is required.
     * <p>Default is {@code true}, leading to an exception thrown in case
     * of the parameter missing in the request. Switch this to {@code false}
     * if you prefer a {@code null} in case of the parameter missing.
     * <p>Alternatively, provide a {@link #defaultValue() defaultValue},
     * which implicitly sets this flag to {@code false}.
     */
    boolean required() default true;

    /**
     * The default value to use as a fallback when the request parameter value
     * is not provided or empty. Supplying a default value implicitly sets
     * {@link #required()} to false.
     */
    String defaultValue() default ValueConstants.DEFAULT_NONE;
}