package com.lol.domain;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Board_Comment {

    @ApiModelProperty(example = "1")
    private int id;

    @ApiModelProperty(example = "1")
    private int boardId;

    @ApiModelProperty(example = "댓글 내용")
    private String content;

    @ApiModelProperty(example = "댓글 작성자")
    private String username;

    @ApiModelProperty(example = "댓글 작성 시간")
    private String writeTime;
}
