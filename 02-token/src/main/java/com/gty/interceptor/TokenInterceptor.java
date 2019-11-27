package com.gty.interceptor;

import com.gty.utils.TokenUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("===============使用interceptor==============");
        //获取token
        String token = request.getHeader(TokenUtil.tokenHeader);
        System.out.println("token===================="+token);
        if (null==token) {
            response.sendRedirect("http://127.0.0.1:8080/login");
            System.out.println("没有携带token");
            return false;
        }
        try {
            TokenUtil.getTokenBody(token);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("http://127.0.0.1:8080/login");
            System.out.println("token有问题");
            return false;
        }
        return true;

    }
}
