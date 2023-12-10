package com.lol.dto.detail;

import com.lol.domain.Combination_Comment;
import lombok.Data;

import java.util.List;

@Data
public class Combination_CommentDto {

    private int comsaveId;
    private String content;
    private int page;
    private List<Combination_Comment> commentList;

    public Combination_CommentDto(List<Combination_Comment> commentList, int page) {
        this.commentList = commentList;
        this.page = page;
    }
}
