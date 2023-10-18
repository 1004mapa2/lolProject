package com.lol.controller;

import com.google.gson.JsonArray;
import com.lol.service.LolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
public class LolController {

    private final LolService lolService;

    @GetMapping("/1")
    public void getChallengerGame() {
        String tier = "CHALLENGER";
        JsonArray summonerIdDto = lolService.getSummonerId(tier);
        lolService.insertDB(summonerIdDto, tier);
    }

    @GetMapping("/2")
    public void getGrandmasterGame() {
        String tier = "GRANDMASTER";
        JsonArray summonerIdDto = lolService.getSummonerId(tier);
        lolService.insertDB(summonerIdDto, tier);
    }

    @GetMapping("/3")
    public void getChampionId(){
        lolService.insertChampionId();
    }

    @GetMapping("/4")
    public void testMethod(){

    }
}
