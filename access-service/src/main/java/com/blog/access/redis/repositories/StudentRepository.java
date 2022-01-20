package com.blog.access.redis.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.blog.access.redis.entity.StudentCache;

@Repository
public interface StudentRepository extends CrudRepository<StudentCache, String> {
}