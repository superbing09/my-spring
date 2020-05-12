package com.formwork.context.support;

import com.formwork.beans.BeanDefinition;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Administrator on 2020/4/26.
 * 配置文件查找，解析
 */
public class BeanDefinitionReader {

    private Properties config = new Properties();

    private List<String> registyBeanClasses = new ArrayList<String>();

    private final String SCAN_PACKAGE = "scanPackage";

    private void loadBeanDefinitions(String packageNames) {
        String[] packageNameArray = packageNames.split(",");
        for (String packageName : packageNameArray) {
            URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
            File classDir = new File(url.getFile());

            for (File file : classDir.listFiles()) {
                if (file.isDirectory()) {
                    loadBeanDefinitions(packageName + "." + file.getName());
                } else {
                    registyBeanClasses.add(packageName + "." + file.getName().replaceAll(".class", ""));
                }
            }
        }
    }

    public List<String> loadBeanDefinitions() {
        return registyBeanClasses;
    }

    public BeanDefinitionReader(String... locations) {
        try (InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:", ""))) {
            config.load(inStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadBeanDefinitions(config.getProperty(SCAN_PACKAGE));
    }

    public Properties getConfig() {
        return this.getConfig();
    }

    public BeanDefinition registerBean(String className) {
        if (this.registyBeanClasses.contains(className)) {
            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setBeanClassName(className);
            beanDefinition.setFactoryBeanName(lowerFirstCase(className.substring(className.lastIndexOf(".") + 1)));
            return beanDefinition;
        }
        return null;
    }

    private String lowerFirstCase(String beanName) {
        char[] c = beanName.toCharArray();
        c[0] += 32;
        return String.valueOf(c);
    }
}
