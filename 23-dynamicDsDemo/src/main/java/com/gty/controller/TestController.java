package com.gty.controller;

import com.gty.domain.UserOne;
import com.gty.domain.UserTwo;
import com.gty.service.UserOneService;
import com.gty.service.UserTwoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private UserOneService userOneService;

    @Autowired
    private UserTwoService userTwoService;

    @RequestMapping("/test01")
    public String test01() {
        return "one";
    }

    @RequestMapping("/test02")
    public List<UserOne> test02() {
        return userOneService.getAll();
    }

    @RequestMapping("/test03")
    public List<UserTwo> test03() {
        return userTwoService.getAllTwo();
    }
}
