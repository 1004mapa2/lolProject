package com.lol.service;

import com.lol.domain.ChampionName;
import com.lol.domain.Combination_Comment;
import com.lol.domain.UserAccount;
import com.lol.dto.detail.ChampionsDto;
import com.lol.dto.detail.Combination_CommentDto;
import com.lol.dto.main.ReceiveDto;
import com.lol.dto.main.TierDto;
import com.lol.repository.LolMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LolService {

    private final LolMapper mapper;

    /**
     * 입력된 조합 정보 가져오기
     * @param receiveDto(탑, 정글, 미드, 원딜, 서폿, 티어, 정렬 기준)
     * @return List<TierDto>
     */
    public List<TierDto> getTierInfo(ReceiveDto receiveDto) {
        if (receiveDto.getChampionName1().equals("random") || receiveDto.getChampionName1().equals("topIcon")) {
            receiveDto.setChampionName1(null);
        }
        if (receiveDto.getChampionName2().equals("random") || receiveDto.getChampionName2().equals("jungleIcon")) {
            receiveDto.setChampionName2(null);
        }
        if (receiveDto.getChampionName3().equals("random") || receiveDto.getChampionName3().equals("middleIcon")) {
            receiveDto.setChampionName3(null);
        }
        if (receiveDto.getChampionName4().equals("random") || receiveDto.getChampionName4().equals("bottomIcon")) {
            receiveDto.setChampionName4(null);
        }
        if (receiveDto.getChampionName5().equals("random") || receiveDto.getChampionName5().equals("utilityIcon")) {
            receiveDto.setChampionName5(null);
        }

        if(receiveDto.getTier().equals("ALLTIER")) {
            return mapper.getAllTierDtos(receiveDto);
        } else {
            return mapper.getEachTierDtos(receiveDto);
        }
    }

    /**
     * 챔피언 검색 정보 가져오기
     * @param data(입력 문자열)
     * @return List<ChampionName>
     */
    public List<ChampionName> getChampionNameInfo(String data) {

        return mapper.getChampionNameDtos(data);
    }

    /**
     * 상세 페이지 조합 정보 가져오기
     * @param comsaveId(조합 고유 번호)
     * @param tier(티어 기준)
     * @return TierDto
     */
    public TierDto getDetailInfo(int comsaveId, String tier) {
        if(tier.equals("ALLTIER")) {
            return mapper.getDetailInfo(comsaveId);
        } else {
            return mapper.getDetailInfo_tier(comsaveId, tier);
        }
    }

    /**
     * 챔피언 5개 한글 이름 가져오기
     * @param comsaveId(조합 고유 번호)
     * @return List<String>
     */
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

    /**
     * 조합 상세보기 한줄 코멘트 가져오기
     * @param comsaveId(조합 고유 번호)
     * @param page(한줄 코멘트 페이지 번호)
     * @return Combination_CommentDto
     */
    public Combination_CommentDto getComment(int comsaveId, int page) {
        int numberOfPage = 5;
        int showPage = (page - 1) * numberOfPage;

        List<Combination_Comment> commentList = mapper.getComment(comsaveId, showPage, numberOfPage);
        int maxPage = (int) Math.ceil((double) mapper.getMaxPage(comsaveId) / numberOfPage);

        return new Combination_CommentDto(commentList, maxPage);
    }

    /**
     * 한줄 코멘트 등록
     * @param commentDto(코멘트 내용)
     * @param username(유저 아이디)
     */
    public void postComment(Combination_CommentDto commentDto, String username) {
        LocalDateTime now = LocalDateTime.now();
        String formatNowTime = now.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm"));

        Combination_Comment combinationComment = new Combination_Comment();
        combinationComment.setContent(commentDto.getContent());
        combinationComment.setComsaveId(commentDto.getComsaveId());
        combinationComment.setUsername(username);
        combinationComment.setWriteTime(formatNowTime);

        mapper.postComment(combinationComment);
    }

    /**
     * 한줄 코멘트 삭제
     * @param commentId(댓글 고유 번호)
     * @param authentication(사용자 정보)
     */
    public void deleteComment(int commentId, Authentication authentication) {
        Combination_Comment comment = mapper.findByComment(commentId);
        if(comment.getUsername().equals(authentication.getName())) {
            mapper.deleteComment(commentId);
        } else {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if(authority.getAuthority().equals("ROLE_ADMIN")) {
                    mapper.deleteComment(commentId);
                }
            }
        }
    }

    /**
     * 자신이 등록한 코멘트 체크(삭제 처리하기 위함)
     * @param authentication(사용자 정보)
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
}
