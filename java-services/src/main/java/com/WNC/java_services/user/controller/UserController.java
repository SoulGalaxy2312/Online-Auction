package com.WNC.java_services.user.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.WNC.java_services.user.domain.User;
import com.WNC.java_services.user.dto.request.ChangePasswordRequest;
import com.WNC.java_services.user.dto.request.UpdateUserInfoRequest;
import com.WNC.java_services.user.dto.response.UserResponse;
import com.WNC.java_services.user.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if(user.isEmpty()) return ResponseEntity.notFound().build();
        UserResponse response = new UserResponse();
        user.ifPresent(u -> {
            response.setId(u.getId());
            response.setFullName(u.getFullName());
            response.setEmail(u.getEmail());
            response.setBirthDay(u.getBirthDay());
            response.setRole(u.getRole());
            response.setTotalNumberGoodReviews(u.getTotalNumberGoodReviews());
            response.setTotalNumberReviews(u.getTotalNumberReviews());
            response.setEmailVerified(u.getEmailVerified());
        });
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @RequestBody UpdateUserInfoRequest request) {
        User user =
                userService.updateUserInfo(id, request.getFullName(), request.getBirthDay());
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setBirthDay(user.getBirthDay());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id,
                                               @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}