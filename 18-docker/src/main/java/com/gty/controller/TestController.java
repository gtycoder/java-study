package com.gty.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/one")
    public String test01() {
        return "说明docker容器发布成功了.........";
    }
}
