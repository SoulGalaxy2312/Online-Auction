package com.WNC.java_services.auth.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.WNC.java_services.auth.domain.OtpToken;

import java.util.Optional;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findByEmailAndOtpCodeAndUsedFalse(String email, String otpCode);
}