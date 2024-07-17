package com.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration.LettuceClientConfigurationBuilder;

import io.lettuce.core.resource.DefaultClientResources;
import io.lettuce.core.resource.ClientResources;

import java.time.Duration;

@Configuration
//@PropertySource("classpath:application.properties")
@RefreshScope
public class RedisConfig {

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Value("${redis.password}")
    private String redisPassword;

    @Value("${redis.username}")
    private String redisUsername;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(Environment environment) {
        LettuceClientConfigurationBuilder builder = LettuceClientConfiguration.builder();
        builder.commandTimeout(Duration.ofMillis(200));
        builder.shutdownTimeout(Duration.ofMillis(200));

        ClientResources clientResources = DefaultClientResources.builder()
                .ioThreadPoolSize(4)
                .computationThreadPoolSize(4)
                .build();

        builder.clientResources(clientResources);

        LettuceClientConfiguration clientConfiguration = builder.build();

        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(
                environment.getProperty("redis.host", redisHost),
                Integer.parseInt(environment.getProperty("redis.port", String.valueOf(redisPort)))
        );
        redisConfig.setPassword(environment.getProperty("redis.password", redisPassword));
        redisConfig.setUsername(environment.getProperty("redis.username", redisUsername));

        return new LettuceConnectionFactory(redisConfig, clientConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}
