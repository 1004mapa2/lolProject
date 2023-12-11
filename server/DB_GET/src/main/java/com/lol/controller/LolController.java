package com.lol.controller;

import com.google.gson.Gson;
import com.lol.domain.ChampionName;
import com.lol.domain.UserAccount;
import com.lol.dto.detail.Combination_CommentDto;
import com.lol.dto.main.ReceiveDto;
import com.lol.dto.main.TierDto;
import com.lol.service.LolService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LolController {

    private final LolService lolService;
    @PostMapping("/getTierInfo")
    public List<TierDto> getTierInfo(@RequestBody ReceiveDto data){

        return lolService.getTierInfo(data);
    }

    @PostMapping("/getChampionNameInfo")
    public List<ChampionName> getChampionNameInfo(@RequestBody String data){
        Gson gson = new Gson();
        String reData = gson.fromJson(data, String.class);

        return lolService.getChampionNameInfo(reData);
    }

    @GetMapping("/getDetailInfo")
    public TierDto getDetailInfo(@RequestParam("comsaveId") int comsaveId, @RequestParam("tier") String tier){
        TierDto detailInfo = lolService.getDetailInfo(comsaveId, tier);
        detailInfo.setChampionKorNames(lolService.get5ChampionName(comsaveId));

        return detailInfo;
    }

    @GetMapping("/getDetailInfoDynamic")
    public TierDto getDetailInfoDynamic(@RequestParam("comsaveId") int comsaveId, @RequestParam("tier") String tier){

        return lolService.getDetailInfo(comsaveId, tier);
    }

    @PostMapping("/saveComment")
    public void saveComment(@RequestBody Combination_CommentDto commentDto, Authentication authentication) {
        lolService.saveComment(commentDto, authentication.getName());
    }

    @GetMapping("/getComment")
    public Combination_CommentDto getComment(@RequestParam("comsaveId") int comsaveId, @RequestParam("page") int page) {

        return lolService.getComment(comsaveId, page);
    }

    @DeleteMapping("/deleteComment/{commentId}")
    public void deleteComment(@PathVariable int commentId) {
        lolService.deleteComment(commentId);
    }

    @GetMapping("/checkUser")
    public UserAccount checkUser(Authentication authentication) {
        if(authentication != null) {
            return lolService.checkUser(authentication);
        }
        return null;
    }
}
