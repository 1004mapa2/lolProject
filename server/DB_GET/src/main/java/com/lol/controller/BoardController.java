package com.lol.controller;

import com.lol.domain.UserAccount;
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

    @GetMapping("/getBoard/{boardId}")
    public BoardViewDto getBoard(@PathVariable("boardId") int boardId, HttpServletRequest req, HttpServletResponse res) {

        return boardService.getBoard(boardId, req, res);
    }

    @GetMapping("/getBoardUpdateData")
    public PostBoardDto getBoardUpdateData(@RequestParam("boardId") int boardId) {

        return boardService.getBoardUpdateData(boardId);
    }

    @GetMapping("/checkUser")
    public UserAccount checkUser(Authentication authentication) {
        if(authentication != null) {
            return boardService.checkUser(authentication);
        }
        return null;
    }

    @GetMapping("/checkBoardUser")
    public int checkBoardUser(@RequestParam("boardId") int boardId, Authentication authentication) {

        return boardService.checkBoardUser(boardId, authentication);
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

    @DeleteMapping("/deleteComment/{commentId}")
    public void deleteComment(@PathVariable int commentId) {
        boardService.deleteComment(commentId);
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
