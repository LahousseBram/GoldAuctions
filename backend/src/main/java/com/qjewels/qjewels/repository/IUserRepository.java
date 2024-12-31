package com.qjewels.qjewels.repository;

import com.qjewels.qjewels.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailVerificationToken(String token);

}