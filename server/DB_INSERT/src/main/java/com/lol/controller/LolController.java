package com.lol.controller;

import com.google.gson.JsonArray;
import com.lol.service.LolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LolController {

    private final LolService lolService;

    @GetMapping("/1")
    public void getUserInfo() {
        lolService.insertDB_userInfo();
    }

    @GetMapping("/2")
    public void getChallengerGame() {
        String tier = "CHALLENGER";
        lolService.insertDB_matchInfo(tier);
        lolService.moveDB_originalToTier();
    }

    @GetMapping("/3")
    public void getGrandmasterGame() {
        String tier = "GRANDMASTER";
        lolService.insertDB_matchInfo(tier);
    }

    @GetMapping("/4")
    public void getChampionId(){
        lolService.insertChampionId();
    }

    @GetMapping("/5")
    public void testMethod(){
    }
}
