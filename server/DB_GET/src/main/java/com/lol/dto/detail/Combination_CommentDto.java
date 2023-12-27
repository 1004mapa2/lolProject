package com.lol.dto.detail;

import com.lol.domain.Combination_Comment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class Combination_CommentDto {

    @ApiModelProperty(example = "4480")
    private int comsaveId;

    @ApiModelProperty(example = "댓글 내용")
    private String content;

    @ApiModelProperty(example = "1")
    private int page;

    @ApiModelProperty(example = "댓글 목록")
    private List<Combination_Comment> commentList;

    public Combination_CommentDto(List<Combination_Comment> commentList, int page) {
        this.commentList = commentList;
        this.page = page;
    }
}
