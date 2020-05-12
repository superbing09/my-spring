package com.formwork.webmvc.servlet;

import com.formwork.annotation.Autowired;
import com.formwork.annotation.Controller;
import com.formwork.annotation.Service;
import com.formwork.context.GPApplicationContext;
import com.mock.controller.HelloController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2020/4/14.
 */
public class DispatchServlet extends HttpServlet {

/*    private Properties contextconfig = new Properties();

    private Map<String, Object> beanMap = new ConcurrentHashMap<String, Object>();

    private List<String> classNames = new ArrayList<String>();*/

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("----------doPost--------------");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        // start init

        GPApplicationContext context = new GPApplicationContext(config.getInitParameter("contextConfigLocation"));
        HelloController controller = (HelloController) context.getBean("helloController");
        controller.hello();
        // handlerMapping

        // 关联@requestMapping
        //initHandlerMapping();
    }

/*    private void doLoadConfig(String location) {
        try (InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(location.replace("classpath:", ""))) {
            contextconfig.load(inStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doScanner(String packageNames) {
        String[] packageNameArray = packageNames.split(",");
        for(String packageName : packageNameArray){
            URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
            File classDir = new File(url.getFile());

            for (File file : classDir.listFiles()) {
                if (file.isDirectory()) {
                    doScanner(packageName + "." + file.getName());
                } else {
                    classNames.add(packageName + "." + file.getName().replaceAll(".class", ""));
                }
            }
        }
    }

    private void doAutowired() {
        if (beanMap.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Autowired.class)) {
                    continue;
                }
                Autowired autowired = field.getAnnotation(Autowired.class);
                String beanName = autowired.value().trim();
                if("".equals(beanName)) {
                    beanName = lowerFirstCase(field.getType().getSimpleName());
                }
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(), beanMap.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }
        try {
            for (String clazzName : classNames) {
                Class<?> clazz = Class.forName(clazzName);
                if (clazz.isAnnotationPresent(Controller.class)) {
                    beanMap.put(lowerFirstCase(clazz.getSimpleName()), clazz.newInstance());
                } else if (clazz.isAnnotationPresent(Service.class)) {
                    Service service = clazz.getAnnotation(Service.class);
                    String beanName = service.value().trim();
                    if ("".equals(beanName)) {
                        beanName = lowerFirstCase(clazz.getSimpleName());
                    }

                    Object instance = clazz.newInstance();
                    beanMap.put(beanName, instance);
                    Class<?>[] interfaces = instance.getClass().getInterfaces();
                    for(Class cls : interfaces) {
                        beanMap.put(lowerFirstCase(cls.getSimpleName()), instance);
                    }
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initHandlerMapping() {
    }

    private String lowerFirstCase(String beanName) {
        char[] c = beanName.toCharArray();
        c[0] += 32;
        return String.valueOf(c);
    }*/
}
