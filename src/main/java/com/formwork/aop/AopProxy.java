package com.formwork.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2020/5/18.
 */
// 使用jdk动态代理
public class AopProxy implements InvocationHandler {

    private AopConfig config;

    private Object target;

    public void setConfig(AopConfig config) {
        this.config = config;
    }

    public Object getProxy(Object instance) {
        this.target = instance;
        Class<?> clazz = instance.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(config.contains(method)) {
            AopConfig.Aspect aspect = config.get(method);
            aspect.getPoints()[0].invoke(aspect.getAspect());
        }
        Object object = method.invoke(this.target, args);
        if(config.contains(method)) {
            AopConfig.Aspect aspect = config.get(method);
            aspect.getPoints()[1].invoke(aspect.getAspect());
        }
        return object;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
