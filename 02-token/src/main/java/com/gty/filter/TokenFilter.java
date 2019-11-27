package com.gty.filter;

import com.gty.utils.TokenUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//并未使用filter测试
//@WebFilter(urlPatterns = "/**")
public class TokenFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("===============使用filter=============");
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;

        //获取token
        String token = request.getHeader(TokenUtil.tokenHeader);
        System.out.println("token====================" + token);
        if (null == token) {
            response.sendRedirect("http://127.0.0.1:8080/login");
            System.out.println("没有携带token");
            return;
        }
        try {
            TokenUtil.getTokenBody(token);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("http://127.0.0.1:8080/login");
            System.out.println("token有问题");
            return;
        }
        filterChain.doFilter(request,response);
    }
}
