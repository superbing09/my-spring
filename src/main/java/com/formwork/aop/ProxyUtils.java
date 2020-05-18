package com.formwork.aop;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2020/5/18.
 */
public class ProxyUtils {

    public static Object getTargetObject(Object proxy) throws Exception {
        // 判断对象是否是代理过的对象

        // 不是代理对象直接返回
        if(!isAopProxy(proxy)) {
            return proxy;
        }
        return getProxyTargetObject(proxy);
    }

    private static boolean isAopProxy(Object object) {
        return Proxy.isProxyClass(object.getClass());
    }

    private static Object getProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field target = aopProxy.getClass().getDeclaredField("target");
        target.setAccessible(true);
        return target.get(aopProxy);
    }
}
