package com.lol.repository;

import com.lol.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface LoginMapper {

    public void registerUser(UserDto userDto);

    public Optional<UserDto> findByUser(String username);

    public void updateUser(String newPassword, String username);
}
