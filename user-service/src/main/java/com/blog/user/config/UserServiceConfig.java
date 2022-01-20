package com.blog.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.blog.commonutill.config.redis.RedisConfig;

@Configuration
@Import(RedisConfig.class)
public class UserServiceConfig {

}
