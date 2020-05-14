package com.formwork.webmvc.servlet;

import com.formwork.annotation.Autowired;
import com.formwork.annotation.Controller;
import com.formwork.annotation.Service;
import com.formwork.context.GPApplicationContext;
import com.formwork.webmvc.HandlerAdapter;
import com.formwork.webmvc.HandlerMapping;
import com.formwork.webmvc.ModelAndView;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2020/4/14.
 */
public class DispatchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    private List<HandlerMapping> handlerMappings = new ArrayList<HandlerMapping>();

    private List<HandlerAdapter> handlerAdapter = new ArrayList<HandlerAdapter>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath,"").replaceAll("/+", "/");
        //HandlerMapping handler = handlerMapping.get(url);
        // 对象，方法名
 /*       try {
            ModelAndView modelAndView = (ModelAndView) handler.getMethod().invoke(handler.getController(),null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }*/

        doDispatch(req, resp);
        System.out.println("----------doPost--------------");
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) {
        HandlerMapping handler = getHandler(req);

        HandlerAdapter adapter = getHandlerAdapter(handler);

        ModelAndView mv = adapter.handle(req, resp, handler);

        processDispatchResult(resp, mv);
    }

    private void processDispatchResult(HttpServletResponse resp, ModelAndView mv) {
        // viewsolvers
    }

    private HandlerAdapter getHandlerAdapter(HandlerMapping handler) {
        return null;
    }

    private HandlerMapping getHandler(HttpServletRequest req) {
        return null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        // start init

        GPApplicationContext context = new GPApplicationContext(config.getInitParameter("contextConfigLocation"));

        initStrategies(context);

        HelloController controller = (HelloController) context.getBean("helloController");
        controller.hello();
        // handlerMapping

        // 关联@requestMapping
        //initHandlerMapping();
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
    }

    private void initRequestToViewNameTranslator(GPApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(GPApplicationContext context) {
    }

    private void initHandlerAdapters(GPApplicationContext context) {
    }

    private void initHandlerMappings(GPApplicationContext context) {

    }

    private void initThemeResolver(GPApplicationContext context) {
    }

    private void initLocaleResolver(GPApplicationContext context) {
    }

    private void initMultipartResolver(GPApplicationContext context) {
    }

}
