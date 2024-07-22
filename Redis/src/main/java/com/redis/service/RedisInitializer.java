package com.redis.service;

import com.redis.config.RedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RedisInitializer {

    private static final Logger logger = LoggerFactory.getLogger(RedisInitializer.class);
    private static AnnotationConfigApplicationContext context;

    public static void initialize(RedisProperties properties) {
        logger.debug("Initializing Redis with properties: {}", properties);

        context = new AnnotationConfigApplicationContext();
        context.registerBean(RedisProperties.class, () -> properties);
        context.scan("com.nst.cache.config");
        context.refresh();

        logger.debug("Redis initialization complete");
    }
}
