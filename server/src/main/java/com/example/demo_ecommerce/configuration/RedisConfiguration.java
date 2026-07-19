package com.example.demo_ecommerce.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfiguration {
    @Bean
    LettuceConnectionFactory lettuceConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6379);
    }

    @Bean
    RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        GenericJacksonJsonRedisSerializer jsonRedisSerializer = GenericJacksonJsonRedisSerializer.builder().build();
        RedisSerializationContext.SerializationPair<String> serializationPair = RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(serializationPair)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonRedisSerializer))
                .entryTtl(Duration.ofMinutes(1))
                .disableCachingNullValues();
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        return RedisCacheManager.builder(redisCacheWriter)
                .cacheDefaults(redisCacheConfiguration)
                .transactionAware()
                .build();
    }

}
