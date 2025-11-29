package com.WNC.java_services.user.dto.response;

import java.time.LocalDate;

import com.WNC.java_services.user.domain.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private LocalDate birthDay;
    private UserRole role;
    private Integer totalNumberReviews;
    private Integer totalNumberGoodReviews;
    private Boolean emailVerified;
}