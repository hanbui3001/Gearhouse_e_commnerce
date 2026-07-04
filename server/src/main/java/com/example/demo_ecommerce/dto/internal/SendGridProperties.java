package com.example.demo_ecommerce.dto.internal;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "sendgrid")
public record SendGridProperties(
    String apiKey,
    String fromEmail,
    String fromName,
    Map<String,String> templateId)
{}
