package com.lol.controller;

import com.lol.dto.UserDto;
import com.lol.repository.RedisRepository;
import com.lol.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class JwtController {

    private final LoginService loginService;
//    private final RedisRepository redisRepository;

    @PostMapping("/registerUser")
    public void join(@RequestBody UserDto userDto) {
        loginService.registerUser(userDto);
    }

    @PostMapping("/usernameDuplicateCheck")
    public int usernameDuplicateCheck(@RequestBody UserDto userDto) {
        int result = loginService.usernameDuplicateCheck(userDto.getUsername());

        return result;
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

    @GetMapping("/logout")
    public void logout(HttpServletRequest request) {
        loginService.logout(request);
    }

//    @GetMapping("/getToken")
//    public String getToken() {
//        String tokens = redisRepository.findAll().toString();
//        return tokens;
//    }
}