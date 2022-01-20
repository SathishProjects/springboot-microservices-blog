package com.blog.commonutill.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.integration.support.locks.LockRegistry;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@AutoConfigureAfter({ RedisLockAutoConfiguration.class, RedisAutoConfiguration.class })
//@ConditionalOnProperty(prefix = "com.blog", name = "type", havingValue = "redis", matchIfMissing = false)
public class CustomRedisLockAutoConfiguration {
	@Autowired
	RedisConnectionFactory redisConnectionFactory;

	@Autowired(required = true)
	@Qualifier("serviceLockRegistry")
	LockRegistry serviceLockRegistry;

	@Autowired
	RedisTemplate<Object, Object> redisTemplate;

	@Bean
	public CustomRedisLockService customRedisLockService() {
		log.info("Inside CustomRedisLockAutoConfiguration");
		return new CustomRedisLockService(redisTemplate, serviceLockRegistry);
	}
}
