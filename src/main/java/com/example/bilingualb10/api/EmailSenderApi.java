package com.example.bilingualb10.api;

import com.example.bilingualb10.dto.SimpleResponse;
import com.example.bilingualb10.service.emailSender.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/emailSender")
@PreAuthorize("hasAuthority('ADMIN')")
public class EmailSenderApi {
    private final EmailService emailService;

    @PostMapping("/send-html-email")
    SimpleResponse sendHtmlEmail(@RequestParam Long userId,@RequestParam Long testId) {
        return emailService.sendHtmlMessage(testId, userId);
    }
}