package com.formwork.webmvc;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2020/5/13.
 */
public class HandlerMapping {
    private Object controller;

    private Method method;

    private Pattern urlPattern;

    public HandlerMapping(Pattern urlPattern, Object controller, Method method) {
        this.controller = controller;
        this.method = method;
        this.urlPattern = urlPattern;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(Pattern urlPattern) {
        this.urlPattern = urlPattern;
    }
}
