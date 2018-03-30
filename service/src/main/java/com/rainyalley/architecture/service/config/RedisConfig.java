package com.rainyalley.architecture.service.config;

import com.rainyalley.architecture.dao.entity.UserDo;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {



    @Bean("userRedisTemplate")
    @Primary
    public RedisTemplate<String, UserDo> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, UserDo> template = new RedisTemplate<String, UserDo>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new GenericToStringSerializer<UserDo>(UserDo.class));
        template.setValueSerializer(new Jackson2JsonRedisSerializer<UserDo>(UserDo.class));
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        redisCacheManager.setDefaultExpiration(3600);
        return redisCacheManager;
    }
}
