package com.lol.dto.board;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PostCommentDto {

    @ApiModelProperty(example = "1")
    private int boardId;

    @ApiModelProperty(example = "댓글 내용")
    private String content;
}
