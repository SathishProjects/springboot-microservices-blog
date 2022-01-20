package com.blog.commonutill.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RedisLockAutoConfiguration {
	@Autowired
	RedisConnectionFactory redisConnectionFactory;

	@Bean
	public LockRegistry serviceLockRegistry() {
		log.info("Inside serviceLockRegistry");
		return new RedisLockRegistry(redisConnectionFactory, "", 120);
	}
}
