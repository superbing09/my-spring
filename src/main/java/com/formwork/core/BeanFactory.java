package com.formwork.core;

/**
 * Created by Administrator on 2020/4/26.
 */
public interface BeanFactory {
    /**
     * 根据名字从ioc容器中获取bean实例
     * @param beanName
     * @return
     */
    Object getBean(String beanName);
}