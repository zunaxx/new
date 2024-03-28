package com.example.bilingualb10.service.emailSender;

import com.example.bilingualb10.dto.SimpleResponse;

public interface EmailService {
    SimpleResponse sendHtmlMessage(Long testId,Long userId);
}