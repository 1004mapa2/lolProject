package com.lol.controller;

import com.lol.dto.UserDto;
import com.lol.dto.user.UserUpdateDto;
import com.lol.repository.RedisRepository;
import com.lol.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class JwtController {

    private final LoginService loginService;

    @PostMapping("/registerUser")
    public void registerUser(@RequestBody UserDto userDto) {
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

    @GetMapping("/init")
    public void init() {
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request) {
        loginService.logout(request);
    }

    @GetMapping("/getMyPage")
    public String getMyPage(Authentication authentication) {
        if(authentication != null) {
            return authentication.getName();
        }
        return null;
    }

    @PostMapping("/updateUser")
    public int updateUser(@RequestBody UserUpdateDto userUpdateDto, Authentication authentication) {
        return loginService.updateUser(userUpdateDto, authentication.getName());
    }
}