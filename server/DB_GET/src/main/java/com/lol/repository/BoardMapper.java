package com.lol.repository;

import com.lol.domain.Board;
import com.lol.domain.Board_Comment;
import com.lol.dto.board.BoardListAllInfoDto;
import com.lol.dto.board.PostBoardDto;
import com.lol.dto.board.SearchDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardMapper {

    public List<BoardListAllInfoDto> getBoardList(SearchDto searchDto);

    public void postBoard(Board board);

    public int getMaxPage(SearchDto searchDto);

    public Optional<Board> findByBoard(int boardId);

    public PostBoardDto getBoardUpdateData(int boardId);

    public void deleteBoard(int boardId);

    public void updateBoard(PostBoardDto postBoardDto);

    public String checkBoardUser(int boardId);

    public List<Board_Comment> getComments(int boardId);

    public void postComment(Board_Comment boardComment);

    public void deleteComment(int commentId);

    public void incViewCount(int boardId);

    public Optional<Integer> findByLike(int boardId, String username);

    public void postLike(int boardId, String username);

    public void updateLike(int boardId, String username, int likeAdjust);

    public int getLike(int boardId);
}
