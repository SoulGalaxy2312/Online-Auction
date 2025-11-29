package com.WNC.java_services.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.WNC.java_services.user.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}