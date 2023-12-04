package com.lol.repository;

import com.lol.domain.Board;
import com.lol.dto.board.SearchDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    public List<Board> getBoardList(int startNumber, int numberOfPage);

    public void postBoard(Board board);

//    public List<Board> getTitleBoard(String inputText);

//    public List<Board> getWriterBoard(String inputText);

    public int getMaxPage();

//    public int getMaxPageTitle(String inputText);

//    public int getMaxPageWriter(String inputText);
}
