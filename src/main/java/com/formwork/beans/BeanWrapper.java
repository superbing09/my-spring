package com.formwork.beans;

import com.formwork.core.FactoryBean;

/**
 * Created by Administrator on 2020/4/26.
 */
public class BeanWrapper extends FactoryBean {

    // 观察者模式 支持事件响应
    private BeanPostProcessor postProcessor;
    // 包装后的bean对象
    private Object wrapperInstance;

    // 原装bena对象
    private Object originalInstance;

    public Object getWrappedInstance() {
        return wrapperInstance;
    }

    public BeanWrapper(Object instance) {
        this.wrapperInstance = instance;
    }

    // 返回代理之后的类
    // 可能是$Proxy0
    public Class<?> getWrappedClass() {
        return this.wrapperInstance.getClass();
    }

    public BeanPostProcessor getPostProcessor() {
        return postProcessor;
    }

    public void setPostProcessor(BeanPostProcessor postProcessor) {
        this.postProcessor = postProcessor;
    }
}
