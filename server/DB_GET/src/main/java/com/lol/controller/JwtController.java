package com.lol.controller;

import com.lol.domain.UserAccount;
import com.lol.dto.user.UserUpdateDto;
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
    public void registerUser(@RequestBody UserAccount userAccount) {
        loginService.registerUser(userAccount);
    }

    @PostMapping("/usernameDuplicateCheck")
    public int usernameDuplicateCheck(@RequestBody UserAccount userAccount) {
        int result = loginService.usernameDuplicateCheck(userAccount.getUsername());

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