package com.lol.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Data
@RedisHash(value = "token")
public class Token {

    @Id
    private String refreshToken;

    @Indexed
    private String accessToken;

    @TimeToLive
    private Long expiration;

}
