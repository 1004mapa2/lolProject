package com.lol.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class UserDto {

    private int id;
    private String username;
    private String password;
    private String role;

    public List<String> getRoleList() {
        if(this.role.length() > 0) {
            return Arrays.asList(this.role.split(","));
        }
        return new ArrayList<>();
    }
}
