package com.example.bilingualb10.config.emailSenderConfig;

import com.example.bilingualb10.globalException.BadCredentialException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Async
@Slf4j
public class EmailSenderConfig implements EmailConfigService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendEmailWithHTMLTemplate(String to, String subject, String template, Context context) {
        log.info("SEND EMAIL WITH HTML TEMPLATE ");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,"UTF-8");
        try{
            log.info("EMAIL SENDER CONFIG TRY IN ");
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            log.info("HTML CONTENT PROCESS START");
            String htmlContent = templateEngine.process(template,context);
            log.info("HTML CONTENT PROCESS END");
            messageHelper.setText(htmlContent,true);
            log.info("JAVA MAIL SENDER. SEND MIME MESSAGE");
            javaMailSender.send(mimeMessage);
            log.info("Method SEND is works !!!");
        }catch (MessagingException e){
            log.error("Message HELPER doesn't work !!!");
            throw new BadCredentialException("Error sending: "+e.getMessage());
        }
    }
}