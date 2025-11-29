package com.WNC.java_services.notification.service;

import com.WNC.java_services.notification.dto.EmailRequest;

public interface EmailService {
    void sendEmail(EmailRequest request);
}