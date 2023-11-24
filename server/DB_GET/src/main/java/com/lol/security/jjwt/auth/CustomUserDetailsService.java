package com.lol.security.jjwt.auth;

import com.lol.dto.UserDto;
import com.lol.repository.LoginMapper;
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
        UserDto userDto = optionalUserDto.orElseThrow(() -> new NullPointerException());

        return new CustomUserDetails(userDto);
    }
}
