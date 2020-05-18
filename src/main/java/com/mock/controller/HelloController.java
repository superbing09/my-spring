package com.mock.controller;

import com.formwork.annotation.Autowired;
import com.formwork.annotation.Controller;
import com.formwork.annotation.RequestMapping;
import com.formwork.annotation.RequestParam;
import com.formwork.webmvc.ModelAndView;
import com.mock.service.HelloService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2020/4/14.
 */
@Controller
public class HelloController {

    @Autowired
    private HelloService helloService;

    @RequestMapping("/home")
    public String hello() {
        helloService.sayHello("hello");
        return null;
    }
    @RequestMapping("/test.html")
    public ModelAndView title(@RequestParam("title") String title, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("test.html");
        Map<String, Object> model = new HashMap<>();
        model.put("title", title);
        model.put("title2", "supers");
        modelAndView.setModel(model);
        return modelAndView;
    }
}
