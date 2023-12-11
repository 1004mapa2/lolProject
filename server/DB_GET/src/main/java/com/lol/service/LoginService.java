package com.lol.service;

import com.lol.dto.Token;
import com.lol.domain.UserAccount;
import com.lol.dto.user.UserUpdateDto;
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

    public void registerUser(UserAccount userAccount) {
        // 중복 확인 로직 넣기
        String encryptedPassword = passwordEncoder.encode(userAccount.getPassword());
        userAccount.setPassword(encryptedPassword);
        userAccount.setRole("ROLE_USER");
        mapper.registerUser(userAccount);
    }

    public int usernameDuplicateCheck(String username) {
        Optional<UserAccount> userDto = mapper.findByUser(username);
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

    public int updateUser(UserUpdateDto userUpdateDto, String username) {
        if (passwordEncoder.matches(userUpdateDto.getOriginalPassword(), mapper.findByUser(username).get().getPassword())) {
            String encryptedPassword = passwordEncoder.encode(userUpdateDto.getNewPassword());
            mapper.updateUser(encryptedPassword, username);
            return 1;
        } else {
            return 0;
        }
    }
}
