package com.lol.controller;

import com.google.gson.Gson;
import com.lol.domain.Board;
import com.lol.dto.board.PageBoardDto;
import com.lol.dto.board.PostBoardDto;
import com.lol.dto.board.SearchDto;
import com.lol.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/getBoardList")
    public PageBoardDto getBoardList(@RequestBody String page) {

        return boardService.getBoardList(page);
    }

    @PostMapping("/postBoard")
    public void postBoard(@RequestBody PostBoardDto postBoardDto, Authentication authentication) {
        boardService.postBoard(postBoardDto, authentication.getName());
    }

/*    @PostMapping("/getSearchBoard")
    public List<Board> getSearchBoard(@RequestBody SearchDto searchDto) {
        List<Board> searchBoard = boardService.getSearchBoard(searchDto);

        return searchBoard;
    }*/
}
