package com.lol.controller;

import com.lol.dto.board.*;
import com.lol.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public BoardViewDto getBoard(@RequestParam("boardId") int boardId, HttpServletRequest req, HttpServletResponse res) {

        return boardService.getBoard(boardId, req, res);
    }

    @GetMapping("/getBoardUpdateData")
    public PostBoardDto getBoardUpdateData(@RequestParam("boardId") int boardId) {

        return boardService.getBoardUpdateData(boardId);
    }

    @GetMapping("/checkUser")
    public int checkUser(@RequestParam int boardId, Authentication authentication) {

        return boardService.checkUser(boardId, authentication);
    }

    @DeleteMapping("/deleteBoard/{boardId}")
    public void deleteBoard(@PathVariable int boardId) {
        boardService.deleteBoard(boardId);
    }

    @PatchMapping("/updateBoard/{boardId}")
    public void updateBoard(@PathVariable int boardId, @RequestBody PostBoardDto postBoardDto) {
        boardService.updateBoard(boardId, postBoardDto);
    }

    @PostMapping("/postComment")
    public void postComment(@RequestBody PostCommentDto postCommentDto, Authentication authentication) {
        boardService.postComment(postCommentDto, authentication.getName());
    }

    @GetMapping("/getLike")
    public int getLike(@RequestParam("boardId") int boardId) {

        return boardService.getLike(boardId);
    }

    @GetMapping("/getMyLike")
    public int getMyLike(@RequestParam("boardId") int boardId, Authentication authentication) {

        return boardService.getMyLike(boardId, authentication.getName());
    }

    @GetMapping("/postLike")
    public void postLike(@RequestParam("boardId") int boardId, Authentication authentication) {
        boardService.postLike(boardId, authentication.getName());
    }
}
