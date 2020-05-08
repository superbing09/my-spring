package com.formwork.annotation;

/**
 * Created by Administrator on 2020/4/14.
 */
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    String value() default "";
}
