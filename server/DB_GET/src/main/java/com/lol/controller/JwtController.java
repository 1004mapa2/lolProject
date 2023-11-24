package com.lol.controller;

import com.lol.dto.Token;
import com.lol.dto.UserDto;
import com.lol.repository.RedisRepository;
import com.lol.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class JwtController {

    private final LoginService loginService;
    private final RedisRepository redisRepository;

    @PostMapping("/registerUser")
    public void join(@RequestBody UserDto userDto) {
        loginService.registerUser(userDto);
    }

    @PostMapping("/login")
    public void login() {
    }

    @GetMapping("/user")
    public void user() {
    }

    @GetMapping("/manager")
    public void manager() {
    }

    @GetMapping("/admin")
    public void admin() {
    }

    @GetMapping("/init")
    public void test() {
    }

    @GetMapping("/refresh")
    public void refresh() {
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request) {
        loginService.logout(request);
    }

    @GetMapping("/getToken")
    public String getToken() {
        String tokens = redisRepository.findAll().toString();
        return tokens;
    }
}