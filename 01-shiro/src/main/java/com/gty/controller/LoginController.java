package com.gty.controller;

import com.gty.dao.RolesMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class LoginController {
    @Resource
    private RolesMapper rolesMapper;

    @RequestMapping(value = "/notLogin", method = RequestMethod.GET)
    public String notLogin() {
        return "login";
    }

    @RequestMapping("/main")
    public String test() {
        return "main";
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
    public String login(String name, String password) {
        //使用SecurityUtils创建一个当前的用户
        Subject subject = SecurityUtils.getSubject();

        //使用用户的关键信息创建一个token令牌用于验证
        UsernamePasswordToken token = new UsernamePasswordToken(name, password);
        // 执行认证登陆,这里会调用MyRealm中的doGetAuthenticationInfo方法进行身份验证
        subject.login(token);

        //根据权限，指定返回数据
        String role = rolesMapper.getRole(name);
        if ("admin".equals(role)) {
            return "欢迎来到管理员页面";
        } else if ("user".equals(role)) {
            return "欢迎用户使用";
        } else {
            return "欢迎游客访问";
        }
    }

}