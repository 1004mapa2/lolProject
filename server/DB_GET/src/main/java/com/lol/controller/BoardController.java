package com.lol.controller;

import com.lol.dto.board.*;
import com.lol.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/getBoardList")
    public PageBoardDto getBoardList(@RequestBody SearchDto searchDto) {

        return boardService.getBoardList(searchDto);
    }

    @PostMapping("/postBoard")
    public void postBoard(@RequestBody PostBoardDto postBoardDto, Authentication authentication) {
        boardService.postBoard(postBoardDto, authentication.getName());
    }

    @GetMapping("/getBoard")
    public BoardViewDto getBoard(@RequestParam("boardId") int boardId) {

        return boardService.getBoard(boardId);
    }

    @PostMapping("/postComment")
    public void postComment(@RequestBody PostCommentDto postCommentDto, Authentication authentication) {
        boardService.postComment(postCommentDto, authentication.getName());
    }
}
