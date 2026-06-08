package com.example.demo_ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DemoECommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoECommerceApplication.class, args);
    }

}
