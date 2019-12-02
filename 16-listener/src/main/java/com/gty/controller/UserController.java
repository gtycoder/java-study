package com.gty.controller;

import com.gty.server.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserServer userServer;

    @GetMapping("/test01")
    public String addOne() {
        System.out.println("qqq已经存在了会触发listener");
        return userServer.addUser("qqq");
    }

    @GetMapping("/test02")
    public String addTwo(@RequestParam("name")String name) {
        return userServer.addUser(name);
    }
}
