package com.lol.repository;

import com.lol.domain.Board;
import com.lol.domain.Board_Comment;
import com.lol.dto.board.SearchDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    public List<Board> getBoardList(SearchDto searchDto);

    public void postBoard(Board board);

    public int getMaxPage(SearchDto searchDto);

    public Board getBoard(int boardId);

    public List<Board_Comment> getComments(int boardId);

    public void postComment(Board_Comment boardComment);
}
