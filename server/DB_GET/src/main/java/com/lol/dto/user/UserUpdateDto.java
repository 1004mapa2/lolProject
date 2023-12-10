package com.lol.dto.user;

import lombok.Data;

@Data
public class UserUpdateDto {

    private String originalPassword;
    private String newPassword;
}
