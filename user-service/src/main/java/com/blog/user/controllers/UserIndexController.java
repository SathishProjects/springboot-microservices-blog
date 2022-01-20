package com.blog.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.commonutill.config.redis.CustomRedisLockService;
import com.blog.commonutill.config.redis.LockObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserIndexController {

	@Autowired
	CustomRedisLockService customRedisLockService;

	@GetMapping
	public String index() {
		Long timestamp = System.currentTimeMillis() / 1000L;
		LockObject lockObject = customRedisLockService.tryLock("SAMPLE_PROCESS_TYPE", "12345678", timestamp);

		if (lockObject.isLockObtained()) {
			log.info("Redis lock obtained for : {}", lockObject.toString());
			customRedisLockService.unlock(lockObject);
		} else {
			log.info("Redis lock already aquired for : {}", lockObject.toString());
		}
		return "Hello from User Service";
	}
}
