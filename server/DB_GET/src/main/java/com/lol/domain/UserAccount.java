package com.lol.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class UserAccount {

    @ApiModelProperty(example = "1")
    private int id;

    @ApiModelProperty(example = "유저 아이디")
    private String username;

    @ApiModelProperty(example = "유저 비밀번호")
    private String password;

    @ApiModelProperty(example = "유저 권한")
    private String role;

    @ApiModelProperty(example = "유저 권한 목록")
    public List<String> getRoleList() {
        if(this.role.length() > 0) {
            return Arrays.asList(this.role.split(","));
        }
        return new ArrayList<>();
    }
}
