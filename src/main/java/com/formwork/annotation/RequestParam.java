package com.formwork.annotation;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2020/4/14.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    String value() default "";
}
