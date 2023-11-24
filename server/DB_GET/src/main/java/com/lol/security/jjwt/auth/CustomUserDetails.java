package com.lol.security.jjwt.auth;

import com.lol.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private UserDto userDto;

    public CustomUserDetails(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        userDto.getRoleList().forEach(r -> {
            authorities.add(() -> r);
        });
        return authorities;
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
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
