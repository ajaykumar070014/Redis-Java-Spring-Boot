package com.student.Student.config;

import com.redis.config.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomRedisConfig {

    @Bean
    public RedisProperties redisProperties() {
        RedisProperties properties = new RedisProperties();
        properties.setHost("localhost");
        properties.setPort(7999);
        properties.setUsername("default");
        properties.setPassword("zxcvbnm");
        return properties;
    }
}
