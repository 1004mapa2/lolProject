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

    public PageBoardDto getBoardList(SearchDto searchDto) {
        searchDto.setNumberOfPage(10); // 1페이지에 10개씩 출력
        searchDto.setStartNumber((searchDto.getPage() - 1) * searchDto.getNumberOfPage());
        if(searchDto.getSort() != null && searchDto.getSort().equals("제목")) {
            searchDto.setSort("TITLE");
        } else if(searchDto.getSort() != null && searchDto.getSort().equals("작성자")) {
            searchDto.setSort("WRITER");
        }

        List<Board> boardList = mapper.getBoardList(searchDto);
        int maxPage = (int) Math.ceil((double) mapper.getMaxPage(searchDto) / searchDto.getNumberOfPage());

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
}
