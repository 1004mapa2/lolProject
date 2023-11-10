package com.lol.config;

import com.lol.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

//Security Session => Authentication => UserDetails
public class PrincipalDetails implements UserDetails {

    private UserDto userDto;

    public PrincipalDetails(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //해당 User의 권한을 리턴하는 곳
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDto.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return userDto.getPassword();
    }

    @Override
    public String getUsername() {
        return userDto.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() { //이 계정이 만료됐는가
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { //이 계정이 잠겼는가
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { //이 계정의 비밀번호가 일정기간 지났는가
        return true;
    }

    @Override
    public boolean isEnabled() { //이 계정이 활성화 되있는가
        return true;
    }
}
