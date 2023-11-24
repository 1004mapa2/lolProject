package com.lol.service;

import com.lol.dto.Token;
import com.lol.dto.UserDto;
import com.lol.repository.LoginMapper;
import com.lol.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final PasswordEncoder passwordEncoder;
    private final LoginMapper mapper;
    private final RedisRepository redisRepository;
    public void registerUser(UserDto userDto) {
        // 중복 확인 로직 넣기
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encryptedPassword);
        mapper.registerUser(userDto);
    }

    public void logout(HttpServletRequest request) {
        String jws = request.getHeader("Authorization");
        Token token = redisRepository.findByAccessToken(jws).orElseGet(() -> null);
        if(token != null){
            redisRepository.delete(token);
        }
    }
}
