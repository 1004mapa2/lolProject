package com.lol.dto.board;

import lombok.Data;

@Data
public class PostCommentDto {

    private int boardId;
    private String content;
}
