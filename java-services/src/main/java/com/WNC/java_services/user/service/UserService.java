package com.WNC.java_services.user.service;

import java.util.Optional;

import com.WNC.java_services.user.domain.User;

public interface UserService {
    User createUser(User user);
    Optional<User> findByEmail(String email);
    User updateUserInfo(Long userId, String fullName, java.time.LocalDate birthDay);
    User changePassword(Long userId, String oldPassword, String newPassword);
    Optional<User> findById(Long userId);
}