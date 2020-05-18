package com.formwork.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by Administrator on 2020/5/13.
 */
public class HandlerAdapter {

    private Map<String, Integer> paramMapping;

    public HandlerAdapter(Map<String, Integer> paramMapping) {
        this.paramMapping = paramMapping;
    }

    public ModelAndView handle(HttpServletRequest req, HttpServletResponse resp, HandlerMapping handler) throws InvocationTargetException, IllegalAccessException {
        // 根据用户请求的参数信息，跟method中的参数信息进行动态匹配
        // resp 给方法赋值
        // 1 准备好方法形参
        Class<?>[] paramTypes = handler.getMethod().getParameterTypes();
        // 2 拿到自定义命名参数所在位置
        Map<String, String[]> paramMap = req.getParameterMap();
        // 3 构造实参列表
        Object[] paramValues = new Object[paramTypes.length];
        for (Map.Entry<String, String[]> param : paramMap.entrySet()) {
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll("\\s", ",");
            if (this.paramMapping.containsKey(param.getKey())) {
                int index = this.paramMapping.get(param.getKey());
                paramValues[index] = castStringValue(value, paramTypes[index]);
            }
            if(this.paramMapping.containsKey(HttpServletRequest.class.getName())) {
                int reIndex = this.paramMapping.get(HttpServletRequest.class.getName());
                paramValues[reIndex] = req;
            }
            if(this.paramMapping.containsKey(HttpServletResponse.class.getName())) {
                int resIndex = this.paramMapping.get(HttpServletResponse.class.getName());
                paramValues[resIndex] = resp;
            }
        }
        // 4 handler中取出controller metho ,然后反射调用
        Object result = handler.getMethod().invoke(handler.getController(), paramValues);
        if (result == null) {
            return null;
        }
        if (handler.getMethod().getReturnType() == ModelAndView.class) {
            return (ModelAndView) result;
        } else {
            return null;
        }
    }

    private Object castStringValue(String value, Class<?> clazz) {
        if (clazz == String.class) {
            return value;
        }
        if (clazz == Integer.class) {
            return Integer.valueOf(value);
        }
        if (clazz == int.class) {
            return Integer.valueOf(value);
        }
        return value;
    }
}
