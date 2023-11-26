package com.lol.service;

import com.lol.dto.Token;
import com.lol.dto.UserDto;
import com.lol.repository.LoginMapper;
import com.lol.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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
        userDto.setRole("ROLE_USER");
        mapper.registerUser(userDto);
    }

    public int usernameDuplicateCheck(String username) {
        Optional<UserDto> userDto = mapper.findByUsername(username);
        if (userDto.isPresent()) {
            return 1;
        } else {
            return 0;
        }
    }

    public void logout(HttpServletRequest request) {
        String jws = request.getHeader("Authorization");
        Token token = redisRepository.findByAccessToken(jws).orElseGet(() -> null);
        if (token != null) {
            redisRepository.delete(token);
        }
    }
}
