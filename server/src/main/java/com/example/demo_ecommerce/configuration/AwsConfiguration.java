package com.example.demo_ecommerce.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfiguration {
    @Value("${aws.s3.region}")
    private String region;
    @Value("${aws.credentials.access-key}")
    private String accessKey;
    @Value("${aws.credentials.secret-key}")
    private String secretKey;

    @Bean
    S3Client s3Client() {
        Region region = Region.of(this.region);
        return S3Client.builder()
                .region(region)
                .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey))
                .build();
    }
}
