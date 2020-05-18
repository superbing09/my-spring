package com.formwork.context;

import com.formwork.beans.BeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Adminstrator on 2020/5/18.
 */
public class GPDefaultListableBeanFactory extends GPAbstractApplicationContext {

    // bean的配置信息
    protected Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

    @Override
    protected void onRefresh() {

    }
    @Override
    protected void refreshBeanFactory() {

    }
}
