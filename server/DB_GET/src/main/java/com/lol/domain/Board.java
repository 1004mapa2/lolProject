package com.lol.domain;

import lombok.Data;

@Data
public class Board {

    private int id;
    private String title;
    private String content;
    private String writer;
    private String writeTime;
    private int viewCount;
    private int likeCount;
}
