package com.formwork.webmvc;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Administrator on 2020/5/13.
 */
public class ModelAndView {

    public ModelAndView(String viewName, Map<String,?> model) {

    }

    public static void main(String[] args) {

    }


}
class A extends Thread{
    private int num = 10;
    @Override
    public void run() {
        System.out.println("a" + Thread.currentThread().getName() + ":" + num--);
    }
}

class B implements Runnable{
    private int num = 10;

    @Override
    public void run() {
        System.out.println("b" + Thread.currentThread().getName() + ":" + num--);
    }
}