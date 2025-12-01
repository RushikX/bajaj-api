package com.bajaj;

import com.bajaj.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class WebhookSolverApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebhookSolverApplication.class, args);
    }
}

