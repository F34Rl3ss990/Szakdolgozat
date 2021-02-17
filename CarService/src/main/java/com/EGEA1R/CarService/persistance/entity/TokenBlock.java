package com.EGEA1R.CarService.persistance.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@RedisHash(value = "blockedTokens", timeToLive = 86400)
public class TokenBlock implements Serializable {

    private static final long serialVersionUID = 7156526077883281623L;

    @Id
    private String tokenBlockId;
    @Indexed
    private Long userId;
    private String jwtToken;
}
