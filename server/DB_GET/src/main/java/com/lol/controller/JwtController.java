package com.lol.controller;

import com.lol.domain.UserAccount;
import com.lol.dto.user.UserUpdateDto;
import com.lol.service.LoginService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "회원가입", notes = "회원가입 api: (username, password만 입력)")
    public void registerUser(@RequestBody UserAccount userAccount) {
        loginService.registerUser(userAccount);
    }

    @PostMapping("/usernameDuplicateCheck")
    @ApiOperation(value = "아이디 중복체크", notes = "아이디 중복체크 api: (username만 입력)")
    public int usernameDuplicateCheck(@RequestBody UserAccount userAccount) {
        int result = loginService.usernameDuplicateCheck(userAccount.getUsername());

        return result;
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인")
    public void login() {
    }

    @GetMapping("/init")
    @ApiOperation(value = "jwt 검사", notes = "jwt 만료되었는지 검사하고 만료되었다면 refreshToken으로 재발행")
    public void init() {
    }

    @GetMapping("/logout")
    @ApiOperation(value = "로그아웃")
    public void logout(HttpServletRequest request) {
        loginService.logout(request);
    }

    @GetMapping("/getMyPage")
    @ApiOperation(value = "마이페이지 조회", notes = "username을 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticated", value = "true로 설정", dataTypeClass = boolean.class, paramType = "query"),
            @ApiImplicitParam(name = "authorities[0].authority", value = "ROLE_USER로 설정", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "credentials", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "details", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "principal", value = "아이디 입력", dataTypeClass = Object.class, paramType = "query")
    })
    public String getMyPage(Authentication authentication) {
        if(authentication != null) {
            return authentication.getName();
        }
        return null;
    }

    @PostMapping("/updateUser")
    @ApiOperation(value = "비밀번호 변경", notes = "비밀번호 변경 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticated", value = "true로 설정", dataTypeClass = boolean.class, paramType = "query"),
            @ApiImplicitParam(name = "authorities[0].authority", value = "ROLE_USER로 설정", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "credentials", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "details", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "principal", value = "아이디 입력", dataTypeClass = Object.class, paramType = "query")
    })
    public int updateUser(@RequestBody UserUpdateDto userUpdateDto, Authentication authentication) {
        return loginService.updateUser(userUpdateDto, authentication.getName());
    }
}