package com.formwork.beans;

import com.formwork.aop.AopConfig;
import com.formwork.aop.AopProxy;
import com.formwork.core.FactoryBean;

/**
 * Created by Administrator on 2020/4/26.
 */
public class BeanWrapper extends FactoryBean {

    // 观察者模式 支持事件响应
    private BeanPostProcessor postProcessor;
    // 包装后的bean对象
    private Object wrapperInstance;

    private AopProxy aopProxy = new AopProxy();

    // 原装bena对象
    private Object originalInstance;

    public Object getWrappedInstance() {
        return wrapperInstance;
    }

    public BeanWrapper(Object instance) {
        // aop这里开始， 动态代理添加这里
        this.wrapperInstance = aopProxy.getProxy(instance);
        this.originalInstance = instance;
    }

    // 返回代理之后的类
    // 可能是$Proxy0
    public Class<?> getWrappedClass() {
        return this.wrapperInstance.getClass();
    }

    public Object getOriginalInstance() {
        return originalInstance;
    }

    public BeanPostProcessor getPostProcessor() {
        return postProcessor;
    }

    public void setPostProcessor(BeanPostProcessor postProcessor) {
        this.postProcessor = postProcessor;
    }

    public void setAopConfig(AopConfig aopConfig) {
        this.aopProxy.setConfig(aopConfig);
    }
}
