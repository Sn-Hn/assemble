package com.assemble.auth.repository;


import com.assemble.auth.domain.Jwt;
import org.springframework.data.repository.CrudRepository;

public interface JwtRedisRepository extends CrudRepository<Jwt, String> {
}
