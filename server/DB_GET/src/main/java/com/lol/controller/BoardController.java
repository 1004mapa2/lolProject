package com.lol.controller;

import com.lol.domain.UserAccount;
import com.lol.dto.board.*;
import com.lol.service.BoardService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/getBoardList")
    @ApiOperation(value = "전체 게시판 조회", notes = "검색 내용은 필수 X, 정렬 기준은 VIEWCOUNT, WRITETIME, LIKECOUNT, COMMENTCOUNT 중 1개 선택")
    public PageBoardDto getBoardList(@RequestBody SearchDto searchDto) {

        return boardService.getBoardList(searchDto);
    }

    @PostMapping("/postBoard")
    @ApiOperation(value = "게시물 작성")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticated", value = "true로 설정", dataTypeClass = boolean.class, paramType = "query"),
            @ApiImplicitParam(name = "authorities[0].authority", value = "ROLE_USER로 설정", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "credentials", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "details", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "principal", value = "아이디 입력", dataTypeClass = Object.class, paramType = "query")
    })
    public void postBoard(@RequestBody PostBoardDto postBoardDto, Authentication authentication) {
        boardService.postBoard(postBoardDto, authentication.getName());
    }

    @GetMapping("/getBoard/{boardId}")
    @ApiOperation(value = "게시물 조회")
    @ApiImplicitParam(name = "boardId", value = "게시물 고유 번호", dataTypeClass = Integer.class, paramType = "path")
    public BoardViewDto getBoard(@PathVariable("boardId") int boardId, HttpServletRequest req, HttpServletResponse res) {

        return boardService.getBoard(boardId, req, res);
    }

    @GetMapping("/getBoardUpdateData")
    @ApiOperation(value = "게시물 수정")
    @ApiImplicitParam(name = "boardId", value = "게시물 고유 번호", dataTypeClass = Integer.class, paramType = "query")
    public PostBoardDto getBoardUpdateData(@RequestParam("boardId") int boardId) {

        return boardService.getBoardUpdateData(boardId);
    }

    @GetMapping("/checkUser")
    @ApiOperation(value = "사용자 정보 조회", notes = "글 및 댓글 권한 체크 시 사용")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticated", value = "true로 설정", dataTypeClass = boolean.class, paramType = "query"),
            @ApiImplicitParam(name = "authorities[0].authority", value = "ROLE_USER로 설정", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "credentials", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "details", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "principal", value = "아이디 입력", dataTypeClass = Object.class, paramType = "query")
    })
    public UserAccount checkUser(Authentication authentication) {
        if(authentication != null) {
            return boardService.checkUser(authentication);
        }
        return null;
    }

    @GetMapping("/checkBoardUser")
    @ApiOperation(value = "사용자 정보 조회", notes = "게시글 수정 시 사용, 게시물 권한이 있을 시 return 1 없을 시 return 0")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticated", value = "true로 설정", dataTypeClass = boolean.class, paramType = "query"),
            @ApiImplicitParam(name = "authorities[0].authority", value = "ROLE_USER로 설정", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "credentials", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "details", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "principal", value = "아이디 입력", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "boardId", value = "게시물 고유 번호", dataTypeClass = Integer.class, paramType = "query")
    })
    public int checkBoardUser(@RequestParam("boardId") int boardId, Authentication authentication) {

        return boardService.checkBoardUser(boardId, authentication);
    }

    @DeleteMapping("/deleteBoard/{boardId}")
    @ApiOperation(value = "게시물 삭제")
    @ApiImplicitParam(name = "boardId", value = "게시물 고유 번호", dataTypeClass = Integer.class, paramType = "path")
    public void deleteBoard(@PathVariable int boardId) {
        boardService.deleteBoard(boardId);
    }

    @PatchMapping("/updateBoard/{boardId}")
    @ApiOperation(value = "게시물 수정")
    @ApiImplicitParam(name = "boardId", value = "게시물 고유 번호", dataTypeClass = Integer.class, paramType = "path")
    public void updateBoard(@PathVariable int boardId, @RequestBody PostBoardDto postBoardDto) {
        boardService.updateBoard(boardId, postBoardDto);
    }

    @PostMapping("/postComment")
    @ApiOperation(value = "댓글 작성")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticated", value = "true로 설정", dataTypeClass = boolean.class, paramType = "query"),
            @ApiImplicitParam(name = "authorities[0].authority", value = "ROLE_USER로 설정", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "credentials", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "details", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "principal", value = "아이디 입력", dataTypeClass = Object.class, paramType = "query")
    })
    public void postComment(@RequestBody PostCommentDto postCommentDto, Authentication authentication) {
        boardService.postComment(postCommentDto, authentication.getName());
    }

    @DeleteMapping("/deleteComment/{commentId}")
    @ApiOperation(value = "댓글 삭제")
    @ApiImplicitParam(name = "commentId", value = "댓글 고유 번호", dataTypeClass = Integer.class, paramType = "path")
    public void deleteComment(@PathVariable int commentId) {
        boardService.deleteComment(commentId);
    }

    @GetMapping("/getLike")
    @ApiOperation(value = "좋아요 수 조회", notes = "return 좋아요 갯수")
    @ApiImplicitParam(name = "boardId", value = "게시물 고유 번호", dataTypeClass = Integer.class, paramType = "query")
    public int getLike(@RequestParam("boardId") int boardId) {

        return boardService.getLike(boardId);
    }

    @GetMapping("/getMyLike")
    @ApiOperation(value = "사용자 좋아요 조회", notes = "게시물에 좋아요를 눌렀다면 return 1, 좋아요를 누르지 않았다면 return 0")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticated", value = "true로 설정", dataTypeClass = boolean.class, paramType = "query"),
            @ApiImplicitParam(name = "authorities[0].authority", value = "ROLE_USER로 설정", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "credentials", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "details", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "principal", value = "아이디 입력", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "boardId", value = "게시물 고유 번호", dataTypeClass = Integer.class, paramType = "query")
    })
    public int getMyLike(@RequestParam("boardId") int boardId, Authentication authentication) {

        return boardService.getMyLike(boardId, authentication.getName());
    }

    @GetMapping("/postLike")
    @ApiOperation(value = "좋아요 누르기", notes = "이미 눌렀다면 좋아요 수 -1, 안 눌렀다면 좋아요 수 +1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticated", value = "true로 설정", dataTypeClass = boolean.class, paramType = "query"),
            @ApiImplicitParam(name = "authorities[0].authority", value = "ROLE_USER로 설정", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "credentials", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "details", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "principal", value = "아이디 입력", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "boardId", value = "게시물 고유 번호", dataTypeClass = Integer.class, paramType = "query")
    })
    public void postLike(@RequestParam("boardId") int boardId, Authentication authentication) {
        boardService.postLike(boardId, authentication.getName());
    }
}
