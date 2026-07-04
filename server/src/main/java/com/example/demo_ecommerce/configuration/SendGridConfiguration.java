package com.example.demo_ecommerce.configuration;

import com.example.demo_ecommerce.dto.internal.SendGridProperties;
import com.sendgrid.SendGrid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SendGridConfiguration {
    private final SendGridProperties sendGridProperties;
    @Bean
    SendGrid sendGrid() {
        return new SendGrid(sendGridProperties.apiKey());
    }
}
