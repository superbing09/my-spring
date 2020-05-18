package com.mock.aspect;

/**
 * Created by Administrator on 2020/5/18.
 */
public class LogAspect {
    public void before() {
        System.out.println("before");
    }

    public  void after() {
        System.out.println("after");
    }
}
