package com.lol.dto.board;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PostBoardDto {

    @ApiModelProperty(example = "1")
    private int boardId;

    @ApiModelProperty(example = "게시물 제목")
    private String title;

    @ApiModelProperty(example = "게시물 내용")
    private String content;
}
