package com.redis.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedisRegistry {

    @Getter
    private static RedisCacheService redisCacheService;

    @Autowired
    public void setRedisCacheService(RedisCacheService redisCacheService) {
        RedisRegistry.redisCacheService = redisCacheService;
    }
}
