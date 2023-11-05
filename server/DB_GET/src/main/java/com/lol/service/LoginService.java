package com.lol.service;

import com.lol.dto.UserDto;
import com.lol.repository.LoginMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginMapper mapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void registerUser(UserDto userDto){
        userDto.setRole("ROLE_USER");
        String encodePassword = bCryptPasswordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodePassword);
        mapper.registerUser(userDto);
    }
}
