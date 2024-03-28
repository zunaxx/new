package com.example.bilingualb10.service.emailSender.impl;

import com.example.bilingualb10.config.emailSenderConfig.EmailSenderConfig;
import com.example.bilingualb10.dto.SimpleResponse;
import com.example.bilingualb10.dto.resultDto.TestResultResponse;
import com.example.bilingualb10.entity.User;
import com.example.bilingualb10.globalException.NotFoundException;
import com.example.bilingualb10.repository.UserRepository;
import com.example.bilingualb10.repository.dao.daoImpl.ResultDaoImpl;
import com.example.bilingualb10.service.emailSender.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final EmailSenderConfig emailSenderConfig;
    private final UserRepository userRepository;
    private final ResultDaoImpl resultDao;

    @Transactional
    @Override
    public SimpleResponse sendHtmlMessage(Long testId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User is not found !!!");
            return new NotFoundException("There are no any users with this email !!!");
        });
        try {
            TestResultResponse resultByIds = resultDao.getTestResultById(testId, userId);
            Context context = new Context();
            String subject = "Bilingual Team";
            String templateName = "email-template.html";
            context.setVariable("greeting", greeting());
            context.setVariable("userName", user.getFirstName());
            context.setVariable("testTitle", resultByIds.getTestName());
            context.setVariable("usersScore", resultByIds.getFinalScore());
            context.setVariable("maxScore", resultByIds.getMaxScore());
            emailSenderConfig.sendEmailWithHTMLTemplate(user.getEmail(), subject, templateName, context);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
        return SimpleResponse.builder()
                .message("Successfully sent!")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public String greeting() {
        ZoneId zoneId = ZoneId.of("Asia/Bishkek");
        ZonedDateTime dateTime = ZonedDateTime.now(zoneId);
        int hour = dateTime.getHour();
        if (hour >= 6 && hour < 12) {
            return "Good morning, ";
        } else if (hour >= 12 && hour < 18) {
            return "Good afternoon, ";
        } else if (hour >= 18 && hour < 24) {
            return "Good evening, ";
        } else {
            return "Good night, ";
        }
    }
}