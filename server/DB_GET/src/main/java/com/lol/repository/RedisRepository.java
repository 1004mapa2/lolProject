package com.lol.repository;

import com.lol.dto.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RedisRepository extends CrudRepository<Token, String> {
    Optional<Token> findByAccessToken(String accessToken);
}
