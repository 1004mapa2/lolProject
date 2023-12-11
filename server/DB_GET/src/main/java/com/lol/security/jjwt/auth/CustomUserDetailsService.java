package com.lol.security.jjwt.auth;

import com.lol.domain.UserAccount;
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
        Optional<UserAccount> optionalUserDto = mapper.findByUser(username);
        UserAccount userAccount = optionalUserDto.orElseThrow(() -> new UsernameNotFoundException("유저가 없습니다."));

        return new CustomUserDetails(userAccount);
    }
}
