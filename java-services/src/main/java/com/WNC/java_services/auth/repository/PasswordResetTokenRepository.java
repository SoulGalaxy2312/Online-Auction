package com.WNC.java_services.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.WNC.java_services.auth.domain.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByEmailAndTokenAndUsedFalse(String email, String token);
}