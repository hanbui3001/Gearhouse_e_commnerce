package com.example.demo_ecommerce.service.impl;

import com.example.demo_ecommerce.dto.internal.SendGridProperties;
import com.example.demo_ecommerce.exception.CustomException;
import com.example.demo_ecommerce.exception.ErrorCode;
import com.example.demo_ecommerce.service.EmailService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SEND-EMAIL")
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final SendGrid sendGrid;
    private final SendGridProperties sendGridProperties;

    @Value("${client.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    @Override
    public void sendPasswordResetEmail(String email, String token) {
        String resetUrl = frontendUrl + "/reset-password?token=" + token;
        Context context = new Context();
        context.setVariable("resetUrl", resetUrl);
        String html = templateEngine.process("reset-password-email", context);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(sendGridProperties.fromEmail());
            helper.setTo(email);
            helper.setSubject("Reset Your Password");
            helper.setText(html, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException | MailException e) {
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }

    }
    @Override
    @Async
    public void sendEmailBySendGrid(String toEmail, String templateKey, Map<String, Object> dynamicData) {
        String templateId = sendGridProperties.templateId().get(templateKey);
        if (templateId == null || templateId.isBlank()) {
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }

        Email from = new Email(sendGridProperties.fromEmail(), sendGridProperties.fromName());
        Email to = new Email(toEmail);

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setTemplateId(templateId);
        Personalization personalization = new Personalization();
        personalization.addTo(to);
        dynamicData.forEach(personalization::addDynamicTemplateData);
        mail.addPersonalization(personalization);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");

        try {
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            log.info("status code send email: " + response.getStatusCode());
            log.info("headers send email: " + response.getHeaders());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
