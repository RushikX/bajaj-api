package com.bajaj.component;

import com.bajaj.service.WebhookService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WebhookStartupComponent {

    private static final Logger logger = LoggerFactory.getLogger(WebhookStartupComponent.class);
    
    private final WebhookService webhookService;

    public WebhookStartupComponent(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostConstruct
    public void onStartup() {
        logger.info("Application started. Initiating webhook flow...");
        webhookService.processWebhookFlow();
    }
}

