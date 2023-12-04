package com.lol.service;

import com.lol.domain.Board;
import com.lol.dto.board.PageBoardDto;
import com.lol.dto.board.PostBoardDto;
import com.lol.dto.board.SearchDto;
import com.lol.repository.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardMapper mapper;

    public PageBoardDto getBoardList(String page) {
        int intPage = Integer.parseInt(page);
        int numberOfPage = 10;
        int startNumber = (intPage - 1) * numberOfPage;
        List<Board> boardList = mapper.getBoardList(startNumber, numberOfPage);
        int maxPage = (int) Math.ceil((double) mapper.getMaxPage() / numberOfPage);

        PageBoardDto pageBoardDto = new PageBoardDto();
        pageBoardDto.setBoardList(boardList);
        pageBoardDto.setMaxPage(maxPage);

        return pageBoardDto;
    }

    public void postBoard(PostBoardDto postBoardDto, String username) {
        Board board = new Board();
        LocalDateTime now = LocalDateTime.now();
        String formatNowTime = now.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm"));

        board.setTitle(postBoardDto.getTitle());
        board.setContent(postBoardDto.getContent());
        board.setWriter(username);
        board.setWriteTime(formatNowTime);
        board.setLikeCount(0);
        board.setViewCount(0);

        mapper.postBoard(board);
    }

//    public PageBoardDto getSearchBoard(SearchDto searchDto) {
////        if(searchDto.getInputText().equals("")) {
////            return mapper.getBoardList();
////        }
//
//        int numberOfPage = 10;
//        if(searchDto.getSearchSort().equals("제목")) {
//            int maxPageTitle = (int) Math.ceil((double) mapper.getMaxPageTitle(searchDto.getInputText()) / numberOfPage);
//            List<Board> titleBoard = mapper.getTitleBoard(searchDto.getInputText());
//            PageBoardDto pageBoardDto = new PageBoardDto();
//            pageBoardDto.setBoardList(titleBoard);
//            pageBoardDto.setMaxPage(maxPageTitle);
//
//            return pageBoardDto;
//
//        } else if(searchDto.getSearchSort().equals("작성자")) {
//            int maxPageWriter = mapper.getMaxPageWriter(searchDto.getInputText());
//            List<Board> writerBoard = mapper.getWriterBoard(searchDto.getInputText());
//            PageBoardDto pageBoardDto = new PageBoardDto();
//            pageBoardDto.setBoardList(writerBoard);
//            pageBoardDto.setMaxPage(maxPageWriter);
//
//            return pageBoardDto;
//        } else {
//            log.error("정렬 기준이 없음");
//            return null;
//        }
//    }
}
