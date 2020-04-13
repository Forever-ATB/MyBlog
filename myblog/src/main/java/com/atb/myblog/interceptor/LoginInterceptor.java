package com.atb.myblog.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Date 2020/4/4 16:30
 * @Created by : 叶宗海
 * @Description : TODO
 */

public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(request.getSession().getAttribute("user")==null){
            response.sendRedirect("/admin/");
            return false;
        }
        return super.preHandle(request, response, handler);
    }
}
