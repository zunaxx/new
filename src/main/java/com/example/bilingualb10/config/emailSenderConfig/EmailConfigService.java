package com.example.bilingualb10.config.emailSenderConfig;

import org.thymeleaf.context.Context;

public interface EmailConfigService {
    void sendEmailWithHTMLTemplate(String to,String subject, String template, Context context);
}