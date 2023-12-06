package com.lol.dto.board;

import com.lol.domain.Board;
import com.lol.domain.Board_Comment;
import lombok.Data;

import java.util.List;

@Data
public class BoardViewDto {
    private Board board;
    private List<Board_Comment> boardComments;
}
