package com.lol.repository;

import com.lol.domain.UserAccount;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface LoginMapper {

    public void registerUser(UserAccount userAccount);

    public Optional<UserAccount> findByUser(String username);

    public void updateUser(String newPassword, String username);
}
