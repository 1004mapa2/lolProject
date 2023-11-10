package com.lol.repository;

import com.lol.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {

    public void registerUser(UserDto userDto);

    public UserDto findByUsername(String username);
}
