package com.WNC.java_services.auth.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.WNC.java_services.auth.domain.OtpToken;
import com.WNC.java_services.auth.dto.request.LoginRequest;
import com.WNC.java_services.auth.dto.request.RegisterRequest;
import com.WNC.java_services.auth.dto.request.VerifyOtpRequest;
import com.WNC.java_services.auth.dto.response.AuthResponse;
import com.WNC.java_services.auth.repository.OtpTokenRepository;
import com.WNC.java_services.auth.util.JwtUtil;
import com.WNC.java_services.notification.dto.EmailRequest;
import com.WNC.java_services.notification.service.EmailService;
import com.WNC.java_services.user.domain.User;
import com.WNC.java_services.user.service.UserService;

import jakarta.transaction.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final OtpTokenRepository otpTokenRepository;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public AuthServiceImpl(
        UserService userService,
        PasswordEncoder passwordEncoder,
        OtpTokenRepository otpTokenRepository,
        JwtUtil jwtUtil,
        EmailService emailService
    ) {

        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.otpTokenRepository = otpTokenRepository;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        // 1. Lưu User
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setBirthDay(request.getBirthDay());
        user.setPassword(request.getPassword());
        userService.createUser(user);

        // 2. Tạo OTP
        OtpToken otp = new OtpToken();
        otp.setEmail(user.getEmail());
        otp.setOtpCode(generateOtp());
        otp.setExpiredAt(LocalDateTime.now().plusMinutes(10));
        otpTokenRepository.save(otp);

        // 3. Gửi email thông báo OTP
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(user.getEmail());
        emailRequest.setSubject("Xác nhận đăng ký");
        emailRequest.setBody("Mã OTP của bạn là: " + otp.getOtpCode() + "\nHạn dùng: 10 phút");

        // Nếu gửi email thất bại, MailSendException (unchecked) sẽ rollback @Transactional
        emailService.sendEmail(emailRequest);
    }

    @Override
    public AuthResponse verifyOtp(VerifyOtpRequest request) {
        OtpToken otp = otpTokenRepository.findByEmailAndOtpCodeAndUsedFalse(request.getEmail(), request.getOtpCode())
                .orElseThrow(() -> new RuntimeException("OTP invalid or expired"));

        otp.setUsed(true);
        otpTokenRepository.save(otp);

        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmailVerified(true);
        userService.createUser(user); // save

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, null); // refresh token optional
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getEmailVerified()) {
            throw new RuntimeException("Email not verified");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, null);
    }

    @Override
    public void forgotPassword(String email) {
        // TODO: tạo OTP reset password + gửi email
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        // TODO: xác thực OTP + đổi mật khẩu
    }

    private String generateOtp() {
        int code = (int)(Math.random() * 900000) + 100000;
        return String.valueOf(code);
    }
}