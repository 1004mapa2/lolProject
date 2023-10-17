package com.prac.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
    
    @GetMapping("sam")
    public void asdf(){
        System.out.println("prac 에서 실행 됨");
    }
}
