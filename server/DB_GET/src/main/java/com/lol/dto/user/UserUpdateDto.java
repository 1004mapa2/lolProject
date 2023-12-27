package com.lol.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserUpdateDto {

    @ApiModelProperty(example = "기존 비밀번호")
    private String originalPassword;

    @ApiModelProperty(example = "새 비밀번호")
    private String newPassword;
}
