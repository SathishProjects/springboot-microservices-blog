package com.blog.access.controllers;

import java.util.concurrent.locks.Lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.blog.access.redis.entity.StudentCache;
import com.blog.access.redis.repositories.StudentRepository;
import com.blog.commonutill.config.redis.CustomRedisLockService;
import com.blog.commonutill.config.redis.LockObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RefreshScope
@RestController
public class AccessIndexController {

	@Value("${test:default}")
	private String message;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	LockRegistry serviceLockRegistry;

	@Autowired
	CustomRedisLockService customRedisLockService;

	@GetMapping
	public String index() throws InterruptedException {
// 		simple lock
		Lock svcLock = serviceLockRegistry.obtain("test-lock-1");
		svcLock.lockInterruptibly();
		svcLock.unlock();

		Long timestamp = System.currentTimeMillis() / 1000L;
		LockObject lockObject = customRedisLockService.tryLock("SAMPLE_PROCESS_TYPE", "12345678", timestamp);

		if (lockObject.isLockObtained()) {
			log.info("Redis lock obtained for : {}", lockObject.toString());
			StudentCache student = new StudentCache("Eng2015001", "John Doe", StudentCache.Gender.MALE, 1);
			studentRepository.save(student);
			StudentCache retrievedStudent = studentRepository.findById("Eng2015001").get();
			log.info("student from cache : {}", retrievedStudent.toString());
		} else {
			log.info("Redis lock already aquired for : {}", lockObject.toString());
		}
		customRedisLockService.unlock(lockObject);
		return restTemplate.getForObject("http://user-service", String.class) + " + " + message;
	}
}
