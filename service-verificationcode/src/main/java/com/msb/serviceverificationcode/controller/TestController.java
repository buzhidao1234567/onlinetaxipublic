package com.msb.serviceverificationcode.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test2")
    public String test(){
        return "哈哈哈";
    }
}
