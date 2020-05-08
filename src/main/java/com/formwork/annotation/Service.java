package com.formwork.annotation;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2020/4/14.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value() default "";
}
