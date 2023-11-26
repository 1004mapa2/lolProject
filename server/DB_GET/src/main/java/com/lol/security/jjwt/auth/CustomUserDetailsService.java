package com.lol.security.jjwt.auth;

import com.lol.dto.UserDto;
import com.lol.repository.LoginMapper;
import com.lol.security.jjwt.handler.JwtAuthenticationFailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final LoginMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDto> optionalUserDto = mapper.findByUsername(username);
        UserDto userDto = optionalUserDto.orElseThrow(() -> new UsernameNotFoundException("유저가 없습니다."));

        return new CustomUserDetails(userDto);
    }
}
