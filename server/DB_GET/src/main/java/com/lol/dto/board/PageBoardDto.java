package com.lol.dto.board;

import com.lol.domain.Board;
import lombok.Data;

import java.util.List;

@Data
public class PageBoardDto {

    private int maxPage;
    private List<Board> boardList;
    private int viewCount;
}
