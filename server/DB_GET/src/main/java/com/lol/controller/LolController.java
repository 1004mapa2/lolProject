package com.lol.controller;

import com.lol.dto.CountDto;
import com.lol.service.LolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class LolController {

    private final LolService lolService;
    @GetMapping("/1")
    public Optional<CountDto> test(){
        Optional<CountDto> count = lolService.getCount();
        System.out.println(count);
        return count;
    }
}
