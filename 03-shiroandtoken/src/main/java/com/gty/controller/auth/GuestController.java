package com.gty.controller.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guest")
public class GuestController {
    @RequestMapping(value = "/msg", method = RequestMethod.GET)
    public String getMessage() {
        return "欢迎游客";
    }
}

