package com.formwork.annotation;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2020/4/14.
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
}
