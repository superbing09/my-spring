package com.formwork.webmvc.servlet;

import com.formwork.annotation.*;
import com.formwork.aop.ProxyUtils;
import com.formwork.context.GPApplicationContext;
import com.formwork.webmvc.HandlerAdapter;
import com.formwork.webmvc.HandlerMapping;
import com.formwork.webmvc.ModelAndView;
import com.formwork.webmvc.ViewResolver;
import com.mock.controller.HelloController;
import com.mock.service.HelloService;
import com.mock.service.HelloServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2020/4/14.
 */
public class DispatchServlet extends HttpServlet {

    private List<HandlerMapping> handlerMappings = new ArrayList<HandlerMapping>();

    private Map<HandlerMapping, HandlerAdapter> handlerAdapters = new HashMap<HandlerMapping, HandlerAdapter>();

    private List<ViewResolver> viewResolvers = new ArrayList<ViewResolver>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("500 Exception error" + e.getMessage());
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException, InvocationTargetException, IllegalAccessException {

        HandlerMapping handler = getHandler(req);
        if(handler == null) {
            resp.getWriter().write("404 not fund request mapping");
        }

        HandlerAdapter adapter = getHandlerAdapter(handler);
        if(adapter == null) {
            resp.getWriter().write("404 not fund adapter");
        }

        //
        ModelAndView mv = adapter.handle(req, resp, handler);

        // 真正的输出
        processDispatchResult(resp, mv);
    }

    private void processDispatchResult(HttpServletResponse resp, ModelAndView mv) throws IOException {
        if(null == mv) {
            return;
        }
        if(this.viewResolvers.isEmpty()) {
            return;
        }
        for(ViewResolver viewResolver : this.viewResolvers) {
            if (!mv.getViewName().equals(viewResolver.getViewName())) {
                continue;
            }
            String out = viewResolver.viewResolver(mv);
            if(out != null) {
                resp.getWriter().write(out);
            }
        }
    }

    private HandlerAdapter getHandlerAdapter(HandlerMapping handler) {
        if(this.handlerAdapters.isEmpty()) {
            return null;
        }
        return this.handlerAdapters.get(handler);
    }

    private HandlerMapping getHandler(HttpServletRequest req) {
        if(this.handlerMappings.isEmpty()) {
            return null;
        }
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");
        for (HandlerMapping handler : this.handlerMappings) {
            Matcher matcher = handler.getUrlPattern().matcher(url);
            if (!matcher.matches()) {
                continue;
            }
            return handler;
        }
        return  null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        // start init
        GPApplicationContext context = new GPApplicationContext(config.getInitParameter("contextConfigLocation"));
        initStrategies(context);
    }

    // mvc 9大组件
    private void initStrategies(GPApplicationContext context) {
        initMultipartResolver(context);// 文件上传解析器
        initLocaleResolver(context);// 本地化配置
        initThemeResolver(context);// 主题解析

        initHandlerMappings(context);//  用来保存controller中配置的reqeustMapping和method的关系
        initHandlerAdapters(context);//动态赋值转换method参数

        initHandlerExceptionResolvers(context);
        initRequestToViewNameTranslator(context);

        initViewResolvers(context);// 解析视图

        initFlashMapManager(context);
    }

    private void initFlashMapManager(GPApplicationContext context) {
    }

    private void initViewResolvers(GPApplicationContext context) {
        // 页面名字与模版文件关联的问题
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        File templateRootDir = new File(templateRootPath);
        for (File template : templateRootDir.listFiles()) {
            this.viewResolvers.add(new ViewResolver(template.getName(), template));
        }
    }

    private void initRequestToViewNameTranslator(GPApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(GPApplicationContext context) {
    }

    // 参数配置
    private void initHandlerAdapters(GPApplicationContext context) {
        // 将参数的名字和类型按顺序保存
        // 后面反射的时候调用的传递是数组
        // 可以通过记录这些参数的index， 从数组中填值，这样的话，就和参数的顺序无关了
        for (HandlerMapping handlerMapping : this.handlerMappings) {
            // 方法参数列表
            Map<String, Integer> paramMapping = new HashMap<String, Integer>();
            Annotation[][] pa = handlerMapping.getMethod().getParameterAnnotations();
            for (int i = 0; i < pa.length; i++) {
                for (Annotation annotation : pa[i]) {
                    if (annotation instanceof RequestParam) {
                        String paramName = ((RequestParam) annotation).value();
                        if (!"".equals(paramName)) {
                            paramMapping.put(paramName, i);
                        }
                    }
                }
            }
            // 处理request, response
            Class<?>[] paramType = handlerMapping.getMethod().getParameterTypes();
            for (int i = 0; i < pa.length; i++) {
                Class<?> type = paramType[i];
                if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                    paramMapping.put(type.getName(), i);
                }
            }

            this.handlerAdapters.put(handlerMapping, new HandlerAdapter(paramMapping));
        }
    }

    // mappings和方法对应
    private void initHandlerMappings(GPApplicationContext context) {
        String[] beanNames = context.getBeanDefinitionNames();
        try {
            for (String beanName : beanNames) {
                Object proxy = context.getBean(beanName);
                Object controller = ProxyUtils.getTargetObject(proxy);
                Class<?> clazz = controller.getClass();
                if (!clazz.isAnnotationPresent(Controller.class)) {
                    continue;
                }
                // class 类上有ReqeustMapping的话， 获取baseUrl
                String baseUrl = "";
                if (clazz.isAnnotationPresent(RequestMapping.class)) {
                    baseUrl = clazz.getAnnotation(RequestMapping.class).value();
                }
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(RequestMapping.class)) {
                        continue;
                    }
                    String regex = ("/" + baseUrl + "/" + method.getAnnotation(RequestMapping.class).value()).replaceAll("/+", "/");
                    handlerMappings.add(new HandlerMapping(Pattern.compile(regex), controller, method));
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initThemeResolver(GPApplicationContext context) {
    }

    private void initLocaleResolver(GPApplicationContext context) {
    }

    private void initMultipartResolver(GPApplicationContext context) {
    }

}
