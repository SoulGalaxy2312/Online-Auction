package com.WNC.java_services.auth.service;

import com.WNC.java_services.auth.dto.request.LoginRequest;
import com.WNC.java_services.auth.dto.request.RegisterRequest;
import com.WNC.java_services.auth.dto.request.VerifyOtpRequest;
import com.WNC.java_services.auth.dto.response.AuthResponse;

public interface AuthService {
    void register(RegisterRequest request);
    AuthResponse verifyOtp(VerifyOtpRequest request);
    AuthResponse login(LoginRequest request);
    void forgotPassword(String email);
    void resetPassword(String email, String otp, String newPassword);
}