package com.lol.service;

import com.lol.dto.*;
import com.lol.repository.LolMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public List<TierDto> getDetailInfo(String comsaveId, String tier) {
        List<TierDto> loseComsave;
        TierDto selectComsave;
        if(tier.equals("ALLTIER")){
            loseComsave = mapper.getAlltierLoseComsave(comsaveId);
            selectComsave = mapper.getAlltierSelectComsave(comsaveId);
        } else {
            loseComsave = mapper.getLoseComsave(tier, comsaveId);
            selectComsave = mapper.getSelectComsave(tier, comsaveId);
        }
        loseComsave.add(0, selectComsave);

        return loseComsave;
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

    public List<Combination_Comment> getComment(Combination_CommentDto commentDto) {
        int comsaveId = commentDto.getComsaveId();
        List<Combination_Comment> commentList = mapper.getComment(comsaveId);

        return commentList;
    }
}
