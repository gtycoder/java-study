package com.gty.controller;

import com.gty.service.MsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MsController {
    @Autowired
    private MsService msService;

    @GetMapping("/info")
    public String getInfo(String id) {
        return msService.info(id);
    }

    @GetMapping("/order")
    public String getOrder(String id) {
        return msService.order44444(id);
    }
}
