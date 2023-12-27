package com.lol.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Combination_Comment {

    @ApiModelProperty(example = "댓글 PK")
    private int id;

    @ApiModelProperty(example = "조합 고유 번호: (4480)")
    private int comsaveId;

    @ApiModelProperty(example = "유저 아이디")
    private String username;

    @ApiModelProperty(example = "댓글 내용")
    private String content;

    @ApiModelProperty(example = "작성일자")
    private String writeTime;
}
