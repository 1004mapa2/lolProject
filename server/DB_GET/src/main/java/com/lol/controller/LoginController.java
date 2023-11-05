package com.lol.controller;

import com.lol.dto.UserDto;
import com.lol.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/5")
    public void test(@ModelAttribute UserDto userDto){
        loginService.registerUser(userDto);
    }
}
