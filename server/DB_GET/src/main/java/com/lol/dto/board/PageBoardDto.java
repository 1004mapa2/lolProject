package com.lol.dto.board;

import lombok.Data;

import java.util.List;

@Data
public class PageBoardDto {

    private int maxPage;
    private List<BoardListAllInfoDto> boardList;
}
