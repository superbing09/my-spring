package com.formwork.context;

import com.formwork.annotation.Autowired;
import com.formwork.annotation.Controller;
import com.formwork.annotation.Service;
import com.formwork.aop.AopConfig;
import com.formwork.beans.BeanDefinition;
import com.formwork.beans.BeanPostProcessor;
import com.formwork.beans.BeanWrapper;
import com.formwork.context.support.BeanDefinitionReader;
import com.formwork.core.BeanFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2020/4/26.
 */
public class GPApplicationContext extends GPDefaultListableBeanFactory implements BeanFactory {

    private String[] configLocations;

    private BeanDefinitionReader reader;

    // 保证单例
    private Map<String, Object> beanCacheMap = new HashMap<String, Object>();
    // 存储被代理过的对象
    private Map<String, BeanWrapper> beanWrapperMap = new ConcurrentHashMap<String, BeanWrapper>();

    public GPApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        this.onRefresh();
    }

    public void onRefresh() {
        // 定位
        this.reader = new BeanDefinitionReader(configLocations);
        // 加载
        List<String> registyBeanClasses = reader.loadBeanDefinitions();
        // 注册
        doRegisty(registyBeanClasses);
        // 依赖注入
        doAutorited();
    }

    // 依赖注入
    private void doAutorited() {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : this.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                getBean(beanName);
            }
        }
    }

    public void populateBean(String beanName, Object instance) {
        Class clazz = instance.getClass();
        if (!(clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class))) {
            return;
        }

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (!field.isAnnotationPresent(Autowired.class)) continue;

            Autowired autowired = field.getAnnotation(Autowired.class);

            String autowiredName = autowired.value().trim();

            if ("".equals(autowiredName)) {
                autowiredName = field.getType().getSimpleName();
            }
            field.setAccessible(true);
            try {
                if (null == this.beanWrapperMap.get(autowiredName)) {
                    getBean(autowiredName);
                }
                field.set(instance, this.beanWrapperMap.get(autowiredName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    // DI注册道beanDefinitionMap
    private void doRegisty(List<String> registyBeanClasses) {
        try {

            for (String className : registyBeanClasses) {

                Class<?> beanClass = Class.forName(className);
                if (beanClass.isInterface()) {
                    continue;
                }
                BeanDefinition beanDefinition = reader.registerBean(className);
                if (beanDefinition != null) {
                    this.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
                }

                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> i : interfaces) {
                    // 多个实现了类会覆盖， spring会报错， @qualify可以设置不同的名字
                    this.beanDefinitionMap.put(i.getSimpleName(), beanDefinition);
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
    public Object getBean(String beanName) {
        if (null != this.beanWrapperMap.get(beanName)) {
            return this.beanWrapperMap.get(beanName).getWrappedInstance();
        }
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        try {
            synchronized (this.beanWrapperMap) {
                BeanPostProcessor beanPostProcessor = new BeanPostProcessor();
                Object instance = instantionBean(beanDefinition);
                if (instance == null) return null;

                beanPostProcessor.postProcessBeforeInitialization(instance, beanName);

                BeanWrapper beanWrapper = new BeanWrapper(instance);
                beanWrapper.setAopConfig(instantionAopconfig(beanDefinition));
                this.beanWrapperMap.put(beanName, beanWrapper);

                beanPostProcessor.postProcessAfterInitialization(instance, beanName);

                populateBean(beanName, instance);
            }
            return this.beanWrapperMap.get(beanName).getWrappedInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 实例化
    public Object instantionBean(BeanDefinition beanDefinition) {
        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        try {
            if (this.beanCacheMap.containsKey(className)) {
                instance = this.beanCacheMap.get(className);
            } else {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.beanCacheMap.put(className, instance);
            }
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private AopConfig instantionAopconfig (BeanDefinition beanDefinition) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        AopConfig config = new AopConfig();
        String expression = reader.getConfig().getProperty("pointCut");
        String[] before = reader.getConfig().getProperty("aspectBefore").split("\\s");
        String[] after = reader.getConfig().getProperty("aspectAfter").split("\\s");
        String className = beanDefinition.getBeanClassName();
        Class<?> clazz = Class.forName(className);
        Pattern pattern = Pattern.compile(expression);

        Class aspectClass = Class.forName(before[0]);
        Class<?>[] interfaces = clazz.getInterfaces();
        for(Method method : clazz.getMethods()) {
            Matcher matcher = pattern.matcher(method.toString());
            if (matcher.matches()) {
                config.put(interfaces[0].getMethods()[0], aspectClass.newInstance(), new Method[]{aspectClass.getMethod(before[1]), aspectClass.getMethod(after[1])});
            }
        }
        return config;
    }

    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }
}
