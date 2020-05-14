package com.formwork.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2020/5/13.
 */
public class HandlerAdapter {
    public ModelAndView handle(HttpServletRequest req, HttpServletResponse resp, HandlerMapping handler) {
        // 根据用户请求的参数信息，跟method中的参数信息进行动态匹配

        return null;
    }
}
