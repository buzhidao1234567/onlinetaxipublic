package com.msb.apipsaaenger.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

//    @RequestMapping("/rest")
    @GetMapping("/test")
    public String test(){
        return "hahaha";
    }
}
