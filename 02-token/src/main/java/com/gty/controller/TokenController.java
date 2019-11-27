package com.gty.controller;

import com.gty.domain.User;
import com.gty.service.UserService;
import com.gty.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class TokenController{
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public Object login(HttpServletRequest request, HttpServletResponse response, String username) throws LoginException {
        User user = userService.getUserByName(username);
        if (null==user) {
            throw new LoginException("没有此人");
        }
        //生成一个token返回或者是放进响应头中
        String token = TokenUtil.getToken(user.getId(), user.getName());
        return token;
    }

    @GetMapping(value = "/ajax")
    @ResponseBody
    public Object ajax(HttpServletRequest request, HttpServletResponse response, String username,String password){
        System.out.println(username);
        System.out.println(password);
        return "ajax成功了";
    }
}

