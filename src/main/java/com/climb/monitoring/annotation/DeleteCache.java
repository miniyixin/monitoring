package com.climb.monitoring.annotation;

import java.lang.annotation.*;

/**
 * @author xin.yi
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DeleteCache {

    String value();
    String key() default "";
}
