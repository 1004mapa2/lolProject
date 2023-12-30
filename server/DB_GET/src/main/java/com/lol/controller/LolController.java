package com.lol.controller;

import com.google.gson.Gson;
import com.lol.domain.ChampionName;
import com.lol.domain.UserAccount;
import com.lol.dto.detail.Combination_CommentDto;
import com.lol.dto.main.ReceiveDto;
import com.lol.dto.main.TierDto;
import com.lol.service.LolService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LolController {

    private final LolService lolService;

    @PostMapping("/getTierInfo")
    @ApiOperation(value = "조합 조회", notes = "sort와 tier를 기준으로 조합을 조회")
    public List<TierDto> getTierInfo(@RequestBody ReceiveDto data) {

        return lolService.getTierInfo(data);
    }

    @PostMapping("/getChampionNameInfo")
    @ApiOperation(value = "챔피언 검색", notes = "return 검색 결과에 포함된 이름의 챔피언 리스트")
    public List<ChampionName> getChampionNameInfo(@RequestBody String data) {
        Gson gson = new Gson();
        String reData = gson.fromJson(data, String.class);

        return lolService.getChampionNameInfo(reData);
    }

    @GetMapping("/getDetailInfo")
    @ApiOperation(value = "상세 페이지 조합 조회", notes = "return 클릭한 조합 정보")
    public TierDto getDetailInfo(@Parameter(name = "comsaveId", description = "조합 고유 번호", in = ParameterIn.QUERY)
                                 @RequestParam("comsaveId") int comsaveId,
                                 @Parameter(name = "tier", description = "티어 이름", in = ParameterIn.QUERY)
                                 @RequestParam("tier") String tier) {
        TierDto detailInfo = lolService.getDetailInfo(comsaveId, tier);
        detailInfo.setChampionKorNames(lolService.get5ChampionName(comsaveId));

        return detailInfo;
    }

    @GetMapping("/getDetailInfoDynamic")
    @ApiOperation(value = "티어 별 정보 변경", notes = "return 티어 별 승률, 픽 횟수")
    public TierDto getDetailInfoDynamic(@Parameter(name = "comsaveId", description = "조합 고유 번호", in = ParameterIn.QUERY)
                                        @RequestParam("comsaveId") int comsaveId,
                                        @Parameter(name = "tier", description = "티어 이름", in = ParameterIn.QUERY)
                                        @RequestParam("tier") String tier) {

        return lolService.getDetailInfo(comsaveId, tier);
    }

    @GetMapping("/getComment")
    @ApiOperation(value = "댓글 목록 조회", notes = "return 선택한 조합 댓글 목록")
    public Combination_CommentDto getComment(@Parameter(name = "comsaveId", description = "조합 고유 번호", in = ParameterIn.QUERY)
                                             @RequestParam("comsaveId") int comsaveId,
                                             @Parameter(name = "page", description = "조회할 페이지 번호", in = ParameterIn.QUERY)
                                             @RequestParam("page") int page) {

        return lolService.getComment(comsaveId, page);
    }

    @PostMapping("/postComment")
    @ApiOperation(value = "댓글 작성")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticated", value = "true로 설정", dataTypeClass = boolean.class, paramType = "query"),
            @ApiImplicitParam(name = "authorities[0].authority", value = "ROLE_USER로 설정", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "credentials", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "details", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "principal", value = "아이디 입력", dataTypeClass = Object.class, paramType = "query")
    })
    public void postComment(@RequestBody Combination_CommentDto commentDto, Authentication authentication) {
        if (authentication != null) {
            lolService.postComment(commentDto, authentication.getName());
        }
    }

    @DeleteMapping("/deleteComment/{commentId}")
    @ApiOperation(value = "댓글 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticated", value = "true로 설정", dataTypeClass = boolean.class, paramType = "query"),
            @ApiImplicitParam(name = "authorities[0].authority", value = "ROLE_USER로 설정", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "commentId", value = "댓글 고유 번호", dataTypeClass = Integer.class, paramType = "path"),
            @ApiImplicitParam(name = "credentials", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "details", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "principal", value = "아이디 입력", dataTypeClass = Object.class, paramType = "query")
    })
    public void deleteComment(@PathVariable int commentId, Authentication authentication) {
        lolService.deleteComment(commentId, authentication);
    }

    @GetMapping("/checkUser")
    @ApiOperation(value = "사용자 정보 조회", notes = "return 아이디, 권한")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticated", value = "true로 설정", dataTypeClass = boolean.class, paramType = "query"),
            @ApiImplicitParam(name = "authorities[0].authority", value = "ROLE_USER로 설정", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "credentials", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "details", value = "null로 설정", dataTypeClass = Object.class, paramType = "query"),
            @ApiImplicitParam(name = "principal", value = "아이디 입력", dataTypeClass = Object.class, paramType = "query")
    })
    public UserAccount checkUser(Authentication authentication) {
        if (authentication != null) {
            return lolService.checkUser(authentication);
        }
        return null;
    }
}
