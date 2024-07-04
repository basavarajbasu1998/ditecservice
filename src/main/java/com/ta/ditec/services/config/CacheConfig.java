package com.ta.ditec.services.config;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;

//import java.time.Duration;
//
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//
//@Configuration
//@EnableCaching
//public class CacheConfig {
//
//	@Bean
//	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//		Jackson2JsonRedisSerializer<Object> jsonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//
//		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1))
//				.disableCachingNullValues()
//				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer));
//
//		return RedisCacheManager.builder(connectionFactory).cacheDefaults(config).build();
//	}
//}

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {

//	@Bean
//	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//		Jackson2JsonRedisSerializer<Object> jsonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//
//		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10))
//				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
//				.disableCachingNullValues();
//
//		return RedisCacheManager.builder(connectionFactory).cacheDefaults(config).build();
//	}

//	@Bean
//    public CacheManager cacheManager() {
//        SimpleCacheManager cacheManager = new SimpleCacheManager();
//
//        // Create and add caches with different names and expiration times
//        cacheManager.setCaches(Arrays.asList(
//            new ConcurrentMapCache("year-total-cache", createCacheConfiguration(60)), // Cache expires after 60 seconds
//            new ConcurrentMapCache("today-total-cache", createCacheConfiguration(30)) // Cache expires after 30 seconds
//        ));
//
//        return cacheManager;
//    }
//
//    private CacheConfiguration createCacheConfiguration(int secondsToExpire) {
//        return CacheConfiguration
//            .builder()
//            .expireAfterWrite(Duration.ofSeconds(secondsToExpire))
//            .build();
//    }

	@Bean
	public CacheManager cacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager();
		Caffeine<Object, Object> caffeine = Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS);
		cacheManager.setCaffeine(caffeine);
		cacheManager.setCacheNames(Arrays.asList("year-total-cache", "today-total-cache"));
		return cacheManager;
	}
}
