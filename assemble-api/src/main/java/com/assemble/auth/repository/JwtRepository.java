package com.assemble.auth.repository;

import com.assemble.auth.domain.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtRepository extends JpaRepository<Jwt, Long> {

    @Query(value = "SELECT jwt FROM Jwt jwt WHERE jwt.refreshToken = :refreshToken")
    Optional<Jwt> findByRefreshToken(@Param(value = "refreshToken") String refreshToken);
}
