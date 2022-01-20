package com.blog.commonutill.config.redis;

import java.util.concurrent.locks.Lock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class LockObject {
	String lockType;
	String lockId;
	Long timeStamp;
	boolean isLockObtained;
	Lock lock;
}
