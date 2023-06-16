package com.assemble.user.repository;

import com.assemble.user.domain.Email;
import com.assemble.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT user FROM User user WHERE user.email = :email")
    Optional<User> findByEmail(@Param(value = "email") Email email);

}
