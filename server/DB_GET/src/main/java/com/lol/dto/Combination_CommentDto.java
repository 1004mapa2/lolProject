package com.lol.dto;

import lombok.Data;

import java.util.List;

@Data
public class Combination_CommentDto {

    private int comsaveId;
    private String content;
    private int page;
    private List<Combination_Comment> commentList;
}
