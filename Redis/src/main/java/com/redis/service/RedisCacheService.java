package com.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.logging.Logger;

@Service
public class RedisCacheService {

    private static final long DEFAULT_CACHE_EXPIRY_SECONDS = 600; // 10 minutes
    private static final Logger logger = Logger.getLogger(RedisCacheService.class.getName());

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(DEFAULT_CACHE_EXPIRY_SECONDS));
        } catch (Exception e) {
            logger.warning("Error setting value in Redis: " + e.getMessage());
            // Handle fallback logic if needed
        }
    }

    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            logger.warning("Error getting value from Redis: " + e.getMessage());
            return null; // Handle as necessary
        }
    }

    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            logger.warning("Error deleting value from Redis: " + e.getMessage());
        }
    }

    public boolean exists(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            logger.warning("Error checking key in Redis: " + e.getMessage());
            return false;
        }
    }
}
