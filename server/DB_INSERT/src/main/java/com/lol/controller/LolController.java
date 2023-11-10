package com.lol.controller;

import com.lol.service.LolService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Scheduled(fixedDelay = 5000)
    @GetMapping("/2")
    public void getGame() {
        lolService.insertDB_matchInfo();
        lolService.moveDB_each_tier_total();
        lolService.moveDB_all_tier_total();
    }

    @GetMapping("/3")
    public void getChampionId(){
        lolService.insertChampionId();
    }
}
