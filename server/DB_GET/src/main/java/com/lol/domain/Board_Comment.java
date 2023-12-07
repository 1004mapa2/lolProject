package com.lol.domain;

import lombok.Data;

@Data
public class Board_Comment {

    private int id;
    private int boardId;
    private String content;
    private String username;
    private String writeTime;
}
