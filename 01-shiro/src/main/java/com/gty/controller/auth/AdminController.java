package com.gty.controller.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @RequestMapping(value = "/msg", method = RequestMethod.GET)
    public String getMessage() {
        return "欢迎管理员======";
    }
}

