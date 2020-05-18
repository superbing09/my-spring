package com.formwork.webmvc;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Administrator on 2020/5/13.
 */
public class ModelAndView {

    private String viewName;
    private Map<String, Object> model;

    public ModelAndView() {
    }
    public ModelAndView(String viewName, Map<String, Object> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

    public String getViewName() {

        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
}
