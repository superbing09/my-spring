package com.mock.controller;

import com.formwork.annotation.Autowired;
import com.formwork.annotation.Controller;
import com.formwork.annotation.RequestMapping;
import com.mock.service.HelloService;

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

}
