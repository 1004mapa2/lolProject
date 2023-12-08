package com.lol.dto.board;

import lombok.Data;

@Data
public class BoardListAllInfoDto {

    private int id;
    private String title;
    private String content;
    private String writer;
    private String writeTime;
    private int viewCount;
    private int likeCount;
    private int commentCount;
}
