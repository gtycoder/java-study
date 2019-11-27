package com.gty.controller;

import com.gty.dao.RolesMapper;
import com.gty.domain.Roles;
import com.gty.util.TokenUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class LoginController {
    @Resource
    private RolesMapper rolesMapper;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String notLogin() {
        return "login";
    }

    @RequestMapping(value = "/noAuthorize")
    @ResponseBody
    public String notRole() {
        return "您没有权限！";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        //注销
        subject.logout();
        return "成功注销！";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(String username, String password, HttpServletRequest request) {
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
            subject.login(usernamePasswordToken);
            Roles role = rolesMapper.getRole(username);
            String token = TokenUtil.getToken(role.getName(), String.valueOf(role.getId()), request.getRemoteAddr());
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

}