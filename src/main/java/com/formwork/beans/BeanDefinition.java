package com.formwork.beans;

/**
 * Created by Administrator on 2020/4/26.
 * 存储配置文件的中信息
 */
public class BeanDefinition {

    private String beanClassName;

    private String factoryBeanName;

    private boolean lazyInit;

    //private boolean isSingle;

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }
}
