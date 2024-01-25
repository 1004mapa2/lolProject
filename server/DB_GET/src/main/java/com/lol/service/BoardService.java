package com.lol.service;

import com.lol.domain.Board;
import com.lol.domain.Board_Comment;
import com.lol.domain.UserAccount;
import com.lol.dto.board.*;
import com.lol.repository.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
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

    /**
     * 게시판 리스트 가져오기
     * @param searchDto(현재 페이지, 글 정렬 기준)
     * @return PageBoardDto
     */
    public PageBoardDto getBoardList(SearchDto searchDto) {
        searchDto.setNumberOfPage(10); // 1페이지에 10개씩 출력
        searchDto.setStartNumber((searchDto.getPage() - 1) * searchDto.getNumberOfPage());
        if (searchDto.getSearchSort() != null && searchDto.getSearchSort().equals("제목")) {
            searchDto.setSearchSort("TITLE");
        } else if (searchDto.getSearchSort() != null && searchDto.getSearchSort().equals("작성자")) {
            searchDto.setSearchSort("WRITER");
        }

        if (searchDto.getSort().equals("최신글순")) {
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
        for (BoardListAllInfoDto boardListAllInfoDto : boardList) {
            LocalDateTime writeTime = LocalDateTime.parse(boardListAllInfoDto.getWriteTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            boardListAllInfoDto.setWriteTime(writeTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm")));
        }

        PageBoardDto pageBoardDto = new PageBoardDto();
        pageBoardDto.setMaxPage(maxPage);
        pageBoardDto.setBoardList(boardList);

        return pageBoardDto;
    }

    /**
     * 게시글 작성
     * @param postBoardDto(게시글 제목, 게시글 내용)
     * @param username(작성자)
     */
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

    /**
     * 게시글 상세 보기
     * @param boardId(게시글 고유 번호)
     * @param req
     * @param res
     * @return BoardViewDto
     */
    public BoardViewDto getBoard(int boardId, HttpServletRequest req, HttpServletResponse res) {

        viewCount(boardId, req, res);

        Optional<Board> optionalBoard = mapper.findByBoard(boardId);
        if (optionalBoard.isPresent()) {
            List<Board_Comment> boardComments = mapper.getComments(boardId);

            // 같은 분에서 정렬하고 초는 빼고 다시 설정
            for (Board_Comment board_Comment : boardComments) {
                LocalDateTime writeTime = LocalDateTime.parse(board_Comment.getWriteTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                board_Comment.setWriteTime(writeTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm")));
            }

            BoardViewDto boardViewDto = new BoardViewDto();
            boardViewDto.setBoard(optionalBoard.get());
            boardViewDto.setBoardComments(boardComments);

            return boardViewDto;
        }
        return null;
    }

    /**
     * 게시글 수정
     * @param boardId(게시글 고유 번호)
     * @return PostBoardDto
     */
    public PostBoardDto getBoardUpdateData(int boardId) {

        return mapper.getBoardUpdateData(boardId);
    }

    /**
     * 로그인한 유저인지 권한 체크
     * @param authentication(유저 정보)
     * @return UserAccount
     */
    public UserAccount checkUser(Authentication authentication) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(authentication.getName());
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            userAccount.setRole(authority.getAuthority());
        }
        return userAccount;
    }

    /**
     * 게시글의 작성자인지 체크
     * @param boardId(게시글 고유 번호)
     * @param authentication(유저 정보)
     * @return int
     */
    public int checkBoardUser(int boardId, Authentication authentication) {
        if (mapper.checkBoardUser(boardId).equals(authentication.getName())) {
            return 1;
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 게시글 삭제
     * @param boardId(게시글 고유 번호)
     */
    public void deleteBoard(int boardId) {
        mapper.deleteBoard(boardId);
    }

    /**
     * 게시글 수정
     * @param boardId(게시글 고유 번호)
     * @param postBoardDto(수정된 게시글 제목, 내용)
     */
    public void updateBoard(int boardId, PostBoardDto postBoardDto) {
        postBoardDto.setBoardId(boardId);
        mapper.updateBoard(postBoardDto);
    }

    /**
     * 댓글 작성
     * @param postCommentDto(게시글 고유번호, 댓글 내용)
     * @param username(작성자)
     */
    public void postComment(PostCommentDto postCommentDto, String username) {
        Optional<Board> optionalBoard = mapper.findByBoard(postCommentDto.getBoardId());
        if (optionalBoard.isPresent()) {
            Board_Comment boardComment = new Board_Comment();
            LocalDateTime now = LocalDateTime.now();
            String formatNowTime = now.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss"));

            boardComment.setBoardId(postCommentDto.getBoardId());
            boardComment.setContent(postCommentDto.getContent());
            boardComment.setWriteTime(formatNowTime);
            boardComment.setUsername(username);

            mapper.postComment(boardComment);
        }
    }

    /**
     * 댓글 삭제
     * @param commentId(댓글 고유 번호)
     */
    public void deleteComment(int commentId) {
        mapper.deleteComment(commentId);
    }

    /**
     * 게시글 좋아요 얻기
     * @param boardId(게시글 고유 번호)
     * @return int
     */
    public int getLike(int boardId) {
        return mapper.getLike(boardId);
    }

    /**
     * 사용자가 게시글에 좋아요 눌렀는지 여부 확인
     * @param boardId(게시글 고유 번호)
     * @param username(사용자 아이디)
     * @return int
     */
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

    /**
     * 좋아요 누르기
     * @param boardId(게시글 고유 번호)
     * @param username(사용자 아이디)
     */
    public void postLike(int boardId, String username) {
        Optional<Board> optionalBoard = mapper.findByBoard(boardId);
        if (optionalBoard.isPresent()) {
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
    }

    /* 메서드 추출 */

    /**
     * 조회수 증가 메서드 쿠키에 boardId가 있다면 조회수 증가 X, boardId가 없다면 조회수 증가 O
     * @param boardId
     * @param req
     * @param res
     */
    private void viewCount(int boardId, HttpServletRequest req, HttpServletResponse res) {
        String cookieValue = "";
        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("boardView")) {
                    cookieValue = cookie.getValue();
                }
            }
            if (!cookieValue.contains("[" + boardId + "]")) {
                mapper.incViewCount(boardId);
                ResponseCookie cookie = ResponseCookie.from("boardView", cookieValue + "_[" + boardId + "]")
                        .path("/")
                        .maxAge(60 * 60 * 24)
                        .sameSite("None")
                        .secure(true)
                        .build();
                System.out.println("1111");
                res.addHeader("Set-Cookie", cookie.toString());
            }
        } else {
            mapper.incViewCount(boardId);
            ResponseCookie cookie = ResponseCookie.from("boardView", "[" + boardId + "]")
                    .path("/")
                    .maxAge(60 * 60 * 24)
                    .sameSite("None")
                    .secure(true)
                    .build();
            System.out.println("2222");
            res.addHeader("Set-Cookie", cookie.toString());
        }
    }
}
