package com.blog.commonutill.config.redis;

import java.util.concurrent.locks.Lock;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.integration.support.locks.LockRegistry;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomRedisLockService {

	RedisTemplate<Object, Object> redisTemplate;

	LockRegistry serviceLockRegistry;

	public CustomRedisLockService(RedisTemplate<Object, Object> redisTemplate, LockRegistry serviceLockRegistry) {
		this.redisTemplate = redisTemplate;
		this.serviceLockRegistry = serviceLockRegistry;
	}

	public LockObject tryLock(@NonNull String processType, @NonNull String id, @NonNull Long timestamp) {
		String lockId = processType.concat(id);
		Lock svcLock = serviceLockRegistry.obtain(lockId);
		boolean isLockObtained = svcLock.tryLock();
		if (isLockObtained) {
			log.info("Lock obtained for id : {}", lockId);
			redisTemplate.opsForValue().set(lockId, timestamp);
			return new LockObject(processType, lockId, timestamp, isLockObtained, svcLock);
		} else {
			log.info("Unable to obtain Lock for id : {}", lockId);
			Long lockTimestamp = (Long) redisTemplate.opsForValue().get(lockId);
			return new LockObject(processType, lockId, lockTimestamp, isLockObtained, svcLock);
		}
	}

	public void unlock(@NonNull LockObject lockObject) {
		try {
			lockObject.getLock().unlock();
			redisTemplate.delete(lockObject.getLockId());
			log.info("Lock released for id : {}", lockObject.getLockId());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
