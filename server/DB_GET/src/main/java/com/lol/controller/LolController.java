package com.lol.controller;

import com.google.gson.Gson;
import com.lol.dto.AllTierDto;
import com.lol.dto.ReceiveDto;
import com.lol.service.LolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class LolController {

    private final LolService lolService;
    @PostMapping("/1")
    public String test(@RequestBody ReceiveDto data){
        List<AllTierDto> allTierInfo = lolService.getAllTierInfo(data);
        Gson gson = new Gson();
        String json = gson.toJson(allTierInfo);

        return json;
    }
}
