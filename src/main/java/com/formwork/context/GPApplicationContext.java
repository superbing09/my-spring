package com.formwork.context;

import com.formwork.beans.BeanDefinition;
import com.formwork.context.support.BeanDefinitionReader;
import com.formwork.core.BeanFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2020/4/26.
 */
public class GPApplicationContext  implements BeanFactory {

    private String[] configLocations;

    private BeanDefinitionReader reader;

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

    public GPApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        this.refresh();
    }
    public void refresh() {
        this.reader = new BeanDefinitionReader(configLocations);
        // 定位
        List<String> beanDefinitions = reader.loadBeanDefinitions();
        // 加载
        doRegisty(beanDefinitions);
        // 注册

        // 依赖注入
    }

    // DI注册道beanDefinitionMap
    private void doRegisty(List<String> beanDefinitions) {
        try {

            for(String className : beanDefinitions) {

                Class<?> beanClass = Class.forName(className);
                if(beanClass.isInterface()) {
                    continue;
                }
                BeanDefinition beanDefinition = reader.registerBean(className);
                if(beanDefinition != null) {
                    this.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
                }

                Class<?>[] interfaces = beanClass.getInterfaces();
                for(Class<?> i : interfaces) {
                    // 多个实现了类会覆盖， spring会报错， @qualify可以设置不同的名字
                    this.beanDefinitionMap.put(i.getName(), beanDefinition);
                }
                // beanName 三种情况
                // 1 默认类名是首字母小写

                // 2 自定义名字

                // 3 接口注入
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // spring会用beanwrapper进行一次包装
    // 装饰器模式
    // 1。保留原来的oop关系
    // 2。可扩展
    @Override
    public Object getBean(String name) {

        return null;
    }
}
