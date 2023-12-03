package com.lol.controller;

import com.google.gson.Gson;
import com.lol.dto.*;
import com.lol.service.LolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LolController {

    private final LolService lolService;
    @PostMapping("/getTierInfo")
    public String getTierInfo(@RequestBody ReceiveDto data){
        Gson gson = new Gson();
        List<TierDto> TierInfo = lolService.getTierInfo(data);
        String json = gson.toJson(TierInfo);

        return json;
    }

    @PostMapping("/getChampionNameInfo")
    public String getChampionNameInfo(@RequestBody String data){
        Gson gson = new Gson();
        String reData = gson.fromJson(data, String.class);
        List<ChampionNameDto> championNameInfo = lolService.getChampionNameInfo(reData);
        String json = gson.toJson(championNameInfo);

        return json;
    }

    @GetMapping("/getDetailInfo")
    public String getDetailInfo(@RequestParam("comsaveId") int comsaveId, @RequestParam("tier") String tier){
        Gson gson = new Gson();
        TierDto detailInfo = lolService.getDetailInfo(comsaveId, tier);
        detailInfo.setChampionKorNames(lolService.get5ChampionName(comsaveId));
        String json = gson.toJson(detailInfo);

        return json;
    }

    @PostMapping("/getDetailInfoDynamic")
    public String getDetailInfoDynamic(@RequestBody DetailDto detailDto){
        Gson gson = new Gson();
        TierDto detailInfo = lolService.getDetailInfo(detailDto.getComsaveId(), detailDto.getTier());
        String json = gson.toJson(detailInfo);

        return json;
    }

    @PostMapping("/saveComment")
    public void saveComment(@RequestBody Combination_CommentDto commentDto, Authentication authentication) {
        lolService.saveComment(commentDto, authentication);
    }

    @PostMapping("/getComment")
    public String getComment(@RequestBody Combination_CommentDto commentDto) {
        Gson gson = new Gson();
        Combination_CommentDto commentData = lolService.getComment(commentDto);
        String json = gson.toJson(commentData);

        return json;
    }
}
