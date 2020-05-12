package com.mock.service;


import com.formwork.annotation.Service;

/**
 * Created by Administrator on 2020/4/14.
 */
@Service
public class HelloServiceImpl implements HelloService{
    @Override
    public void sayHello(String msg) {
        System.out.println("hello: " + msg);;
    }
}
