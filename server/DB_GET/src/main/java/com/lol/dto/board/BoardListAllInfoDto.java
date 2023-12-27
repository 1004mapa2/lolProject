package com.lol.dto.board;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BoardListAllInfoDto {

    @ApiModelProperty(example = "1")
    private int id;

    @ApiModelProperty(example = "게시물 제목")
    private String title;

    @ApiModelProperty(example = "게시물 내용")
    private String content;

    @ApiModelProperty(example = "게시물 작성자")
    private String writer;

    @ApiModelProperty(example = "작성 시간")
    private String writeTime;

    @ApiModelProperty(example = "1")
    private int viewCount;

    @ApiModelProperty(example = "1")
    private int likeCount;

    @ApiModelProperty(example = "0")
    private int commentCount;
}
