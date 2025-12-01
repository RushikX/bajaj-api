package com.bajaj.service;

import com.bajaj.config.AppProperties;
import com.bajaj.dto.WebhookGenerateRequest;
import com.bajaj.dto.WebhookGenerateResponse;
import com.bajaj.dto.WebhookSubmitRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {

    private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);
    
    private final RestTemplate restTemplate;
    private final AppProperties appProperties;
    private final SqlSolverService sqlSolverService;

    public WebhookService(RestTemplate restTemplate, AppProperties appProperties, SqlSolverService sqlSolverService) {
        this.restTemplate = restTemplate;
        this.appProperties = appProperties;
        this.sqlSolverService = sqlSolverService;
    }

    public void processWebhookFlow() {
        try {
            logger.info("Starting webhook flow...");
            
            // Step 1: Generate webhook
            WebhookGenerateResponse response = generateWebhook();
            
            if (response == null || response.getAccessToken() == null || response.getWebhook() == null) {
                logger.error("Failed to generate webhook or missing required fields");
                return;
            }
            
            logger.info("Webhook generated successfully. Webhook URL: {}", response.getWebhook());
            logger.info("Access token received: {}", response.getAccessToken().substring(0, Math.min(20, response.getAccessToken().length())) + "...");
            
            // Step 2: Solve SQL problem
            String sqlQuery = sqlSolverService.solveSqlProblem(response);
            logger.info("SQL query generated: {}", sqlQuery);
            
            // Step 3: Submit solution
            submitSolution(response.getWebhook(), response.getAccessToken(), sqlQuery);
            
            logger.info("Webhook flow completed successfully!");
            
        } catch (Exception e) {
            logger.error("Error processing webhook flow", e);
        }
    }

    private WebhookGenerateResponse generateWebhook() {
        try {
            String url = appProperties.getWebhook().getGenerateUrl();
            
            WebhookGenerateRequest request = new WebhookGenerateRequest(
                appProperties.getUser().getName(),
                appProperties.getUser().getRegNo(),
                appProperties.getUser().getEmail()
            );
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<WebhookGenerateRequest> entity = new HttpEntity<>(request, headers);
            
            logger.info("Sending POST request to: {}", url);
            logger.info("Request body: {}", request);
            
            ResponseEntity<WebhookGenerateResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                WebhookGenerateResponse.class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                logger.error("Failed to generate webhook. Status: {}", response.getStatusCode());
                return null;
            }
            
        } catch (Exception e) {
            logger.error("Exception while generating webhook", e);
            return null;
        }
    }

    private void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        try {
            WebhookSubmitRequest request = new WebhookSubmitRequest(sqlQuery);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + accessToken);
            
            HttpEntity<WebhookSubmitRequest> entity = new HttpEntity<>(request, headers);
            
            logger.info("Submitting solution to: {}", webhookUrl);
            logger.info("SQL Query: {}", sqlQuery);
            
            ResponseEntity<String> response = restTemplate.exchange(
                webhookUrl,
                HttpMethod.POST,
                entity,
                String.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Solution submitted successfully! Response: {}", response.getBody());
            } else {
                logger.error("Failed to submit solution. Status: {}, Response: {}", 
                    response.getStatusCode(), response.getBody());
            }
            
        } catch (Exception e) {
            logger.error("Exception while submitting solution", e);
        }
    }
}

