package com.assemble.auth.repository;


import com.assemble.auth.domain.AccessToken;
import org.springframework.data.repository.CrudRepository;

public interface AccessTokenRedisRepository extends CrudRepository<AccessToken, String> {
}
