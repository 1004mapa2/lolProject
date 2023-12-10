package com.lol.service;

import com.lol.domain.Board;
import com.lol.domain.Board_Comment;
import com.lol.dto.board.*;
import com.lol.repository.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardMapper mapper;

    public PageBoardDto getBoardList(SearchDto searchDto) {
        searchDto.setNumberOfPage(10); // 1페이지에 10개씩 출력
        searchDto.setStartNumber((searchDto.getPage() - 1) * searchDto.getNumberOfPage());
        if (searchDto.getSearchSort() != null && searchDto.getSearchSort().equals("제목")) {
            searchDto.setSearchSort("TITLE");
        } else if (searchDto.getSearchSort() != null && searchDto.getSearchSort().equals("작성자")) {
            searchDto.setSearchSort("WRITER");
        }

        if(searchDto.getSort().equals("최신글순")) {
            searchDto.setSort("WRITETIME");
        } else if (searchDto.getSort().equals("조회순")) {
            searchDto.setSort("VIEWCOUNT");
        } else if (searchDto.getSort().equals("좋아요순")) {
            searchDto.setSort("LIKECOUNT");
        } else if (searchDto.getSort().equals("댓글순")) {
            searchDto.setSort("COMMENTCOUNT");
        }

        List<BoardListAllInfoDto> boardList = mapper.getBoardList(searchDto);
        int maxPage = (int) Math.ceil((double) mapper.getMaxPage(searchDto) / searchDto.getNumberOfPage());

        // 같은 분에서 정렬하고 초는 빼고 다시 설정
        for(BoardListAllInfoDto boardListAllInfoDto : boardList) {
            LocalDateTime writeTime = LocalDateTime.parse(boardListAllInfoDto.getWriteTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            boardListAllInfoDto.setWriteTime(writeTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm")));
        }

        PageBoardDto pageBoardDto = new PageBoardDto();
        pageBoardDto.setMaxPage(maxPage);
        pageBoardDto.setBoardList(boardList);

        return pageBoardDto;
    }

    public void postBoard(PostBoardDto postBoardDto, String username) {
        Board board = new Board();
        LocalDateTime now = LocalDateTime.now();
        String formatNowTime = now.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss"));

        board.setTitle(postBoardDto.getTitle());
        board.setContent(postBoardDto.getContent());
        board.setWriter(username);
        board.setWriteTime(formatNowTime);
        board.setViewCount(0);

        mapper.postBoard(board);
    }

    public BoardViewDto getBoard(int boardId, HttpServletRequest req, HttpServletResponse res) {

        viewCount(boardId, req, res);

        Board board = mapper.getBoard(boardId);
        List<Board_Comment> boardComments = mapper.getComments(boardId);
        BoardViewDto boardViewDto = new BoardViewDto();
        boardViewDto.setBoard(board);
        boardViewDto.setBoardComments(boardComments);

        return boardViewDto;
    }

    public PostBoardDto getBoardUpdateData(int boardId) {

        return mapper.getBoardUpdateData(boardId);
    }

    public int checkUser(int boardId, Authentication authentication) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if(authority.getAuthority().equals("ROLE_ADMIN")) {
                return 1;
            }
        }

        String writerName = mapper.getBoard(boardId).getWriter();
        if (writerName.equals(authentication.getName())) {
            return 1;
        } else {
            return 0;
        }
    }

    public void deleteBoard(int boardId) {
        mapper.deleteBoard(boardId);
    }

    public void updateBoard(int boardId, PostBoardDto postBoardDto) {
        postBoardDto.setBoardId(boardId);
        mapper.updateBoard(postBoardDto);
    }

    public void postComment(PostCommentDto postCommentDto, String username) {
        Board_Comment boardComment = new Board_Comment();
        LocalDateTime now = LocalDateTime.now();
        String formatNowTime = now.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm"));

        boardComment.setBoardId(postCommentDto.getBoardId());
        boardComment.setContent(postCommentDto.getContent());
        boardComment.setWriteTime(formatNowTime);
        boardComment.setUsername(username);

        mapper.postComment(boardComment);
    }

    public int getLike(int boardId) {
        return mapper.getLike(boardId);
    }

    public int getMyLike(int boardId, String username) { //하트문양 변하기용
        Optional<Integer> myLike = mapper.findByLike(boardId, username);
        if (myLike.isEmpty()) {
            return 0;
        } else {
            if (myLike.get() == 1) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public void postLike(int boardId, String username) {
        int likeAdjust = 0;
        Optional<Integer> likeExist = mapper.findByLike(boardId, username);
        if (likeExist.isEmpty()) {
            mapper.postLike(boardId, username);
        } else {
            if (likeExist.get() == 1) {
                mapper.updateLike(boardId, username, likeAdjust);
            } else {
                likeAdjust = 1;
                mapper.updateLike(boardId, username, likeAdjust);
            }
        }
    }

    /* 메서드 추출 */
    private void viewCount(int boardId, HttpServletRequest req, HttpServletResponse res) {
        Cookie oldCookie = null;

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("boardView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + boardId + "]")) {
                mapper.incViewCount(boardId);
                oldCookie.setValue(oldCookie.getValue() + "_[" + boardId + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24); // 24시간
                res.addCookie(oldCookie);
            }
        } else {
            mapper.incViewCount(boardId);
            Cookie newCookie = new Cookie("boardView", "[" + boardId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            res.addCookie(newCookie);
        }
    }
}
