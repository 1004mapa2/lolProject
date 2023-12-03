package com.lol.service;

import com.lol.dto.*;
import com.lol.repository.LolMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LolService {

    private final LolMapper mapper;

    public List<TierDto> getTierInfo(ReceiveDto receiveDto) {
        if (receiveDto.getChampionName1().equals("random") || receiveDto.getChampionName1().equals("emptyBox")) {
            receiveDto.setChampionName1(null);
        }
        if (receiveDto.getChampionName2().equals("random") || receiveDto.getChampionName2().equals("emptyBox")) {
            receiveDto.setChampionName2(null);
        }
        if (receiveDto.getChampionName3().equals("random") || receiveDto.getChampionName3().equals("emptyBox")) {
            receiveDto.setChampionName3(null);
        }
        if (receiveDto.getChampionName4().equals("random") || receiveDto.getChampionName4().equals("emptyBox")) {
            receiveDto.setChampionName4(null);
        }
        if (receiveDto.getChampionName5().equals("random") || receiveDto.getChampionName5().equals("emptyBox")) {
            receiveDto.setChampionName5(null);
        }

        if(receiveDto.getTier().equals("ALLTIER")){
            return mapper.getAllTierDtos(receiveDto);
        } else {
            return mapper.getEachTierDtos(receiveDto);
        }
    }

    public List<ChampionNameDto> getChampionNameInfo(String data) {

        return mapper.getChampionNameDtos(data);
    }

    public TierDto getDetailInfo(int comsaveId, String tier) {
        if(tier.equals("ALLTIER")) {
            return mapper.getDetailInfo(comsaveId);
        } else {
            return mapper.getDetailInfo_tier(comsaveId, tier);
        }
//        List<TierDto> loseComsave;
//        TierDto selectComsave;
//        if(tier.equals("ALLTIER")){
//            loseComsave = mapper.getAlltierLoseComsave(comsaveId);
//            selectComsave = mapper.getAlltierSelectComsave(comsaveId);
//        } else {
//            loseComsave = mapper.getLoseComsave(tier, comsaveId);
//            selectComsave = mapper.getSelectComsave(tier, comsaveId);
//        }
//        loseComsave.add(0, selectComsave);
//
//        return loseComsave;
    }

    public List<String> get5ChampionName(int comsaveId) { // 반복문 쓰는 법 찾아보기
        ChampionsDto championEngNames = mapper.getChampionNames(comsaveId);
        List<String> championKorNames = new ArrayList<>();
        championKorNames.add(mapper.getChampionKorName(championEngNames.getTopName()));
        championKorNames.add(mapper.getChampionKorName(championEngNames.getJungleName()));
        championKorNames.add(mapper.getChampionKorName(championEngNames.getMiddleName()));
        championKorNames.add(mapper.getChampionKorName(championEngNames.getBottomName()));
        championKorNames.add(mapper.getChampionKorName(championEngNames.getUtilityName()));

        return championKorNames;
    }

    public void saveComment(Combination_CommentDto commentDto, Authentication authentication) {
        Combination_Comment combinationComment = new Combination_Comment();
        combinationComment.setContent(commentDto.getContent());
        combinationComment.setComsaveId(commentDto.getComsaveId());
        combinationComment.setUsername(authentication.getName());

        LocalDateTime now = LocalDateTime.now();
        String formatNowTime = now.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm"));
        combinationComment.setWriteTime(formatNowTime);
        mapper.saveComment(combinationComment);
    }

    public Combination_CommentDto getComment(Combination_CommentDto commentDto) {
        int numberOfPage = 5;
        int page = (commentDto.getPage() - 1) * numberOfPage;
        int comsaveId = commentDto.getComsaveId();

        List<Combination_Comment> commentList = mapper.getComment(comsaveId, page, numberOfPage);
        int maxPage = (int) Math.ceil((double) mapper.getMaxPage(comsaveId) / 5);
        commentDto.setCommentList(commentList);
        commentDto.setPage(maxPage);

        return commentDto;
    }
}
