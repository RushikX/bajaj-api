package com.bajaj.service;

import com.bajaj.config.AppProperties;
import com.bajaj.dto.WebhookGenerateRequest;
import com.bajaj.dto.WebhookGenerateResponse;
import com.bajaj.dto.WebhookSubmitRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {

    private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);
    
    private final RestTemplate restTemplate;
    private final AppProperties appProperties;
    private final SqlSolverService sqlSolverService;
    private final ObjectMapper objectMapper;

    public WebhookService(RestTemplate restTemplate, AppProperties appProperties, SqlSolverService sqlSolverService, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.appProperties = appProperties;
        this.sqlSolverService = sqlSolverService;
        this.objectMapper = objectMapper;
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
            
            if (url == null || url.isEmpty()) {
                logger.error("Webhook generate URL is null or empty. Check application.properties configuration.");
                logger.error("AppProperties webhook: {}", appProperties.getWebhook());
                return null;
            }
            
            // Use standard JSON format: {"name": "Y Rushik Kumar", "regNo": "22BRS1271", "email": "rushik7078@gmail.com"}
            String name = appProperties.getUser().getName();
            String regNo = appProperties.getUser().getRegNo();
            String email = appProperties.getUser().getEmail();
            
            WebhookGenerateRequest request = new WebhookGenerateRequest(name, regNo, email);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(java.util.Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("User-Agent", "SpringBoot-WebhookSolver/1.0");
            
            HttpEntity<WebhookGenerateRequest> entity = new HttpEntity<>(request, headers);
            
            // Log the JSON that will be sent
            String jsonBody = objectMapper.writeValueAsString(request);
            logger.info("Sending POST request to: {}", url);
            logger.info("Request body JSON: {}", jsonBody);
            
            try {
                ResponseEntity<WebhookGenerateResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    WebhookGenerateResponse.class
                );
                
                logger.info("Response status: {}", response.getStatusCode());
                
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    return response.getBody();
                } else {
                    logger.error("Failed to generate webhook. Status: {}", response.getStatusCode());
                    return null;
                }
            } catch (HttpServerErrorException e) {
                logger.error("Server error (500) while generating webhook. Response body: {}", e.getResponseBodyAsString());
                logger.error("Status code: {}, Status text: {}", e.getStatusCode(), e.getStatusText());
                logger.error("Response headers: {}", e.getResponseHeaders());
                return null;
            } catch (HttpClientErrorException e) {
                logger.error("Client error (4xx) while generating webhook. Response body: {}", e.getResponseBodyAsString());
                logger.error("Status code: {}, Status text: {}", e.getStatusCode(), e.getStatusText());
                return null;
            } catch (RestClientException e) {
                logger.error("Rest client exception while generating webhook", e);
                return null;
            }
            
        } catch (Exception e) {
            logger.error("Unexpected exception while generating webhook", e);
            return null;
        }
    }

    private void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        try {
            WebhookSubmitRequest request = new WebhookSubmitRequest(sqlQuery);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(java.util.Collections.singletonList(MediaType.APPLICATION_JSON));
            
            // Set Authorization header - requirements show "Authorization : < accessToken >" (raw token, no Bearer prefix)
            String token = accessToken != null ? accessToken.trim() : "";
            headers.set("Authorization", token);
            
            // Also log what we're sending
            logger.info("Token length: {}", token.length());
            logger.info("Token starts with: {}", token.substring(0, Math.min(10, token.length())));
            logger.info("Authorization header format: raw token (no Bearer prefix) as per requirements");
            
            // Log request details (without full token for security)
            logger.info("Submitting solution to: {}", webhookUrl);
            logger.info("Authorization header: {}...", token.substring(0, Math.min(20, token.length())));
            logger.info("SQL Query: {}", sqlQuery);
            
            // Log the JSON being sent
            String jsonBody = objectMapper.writeValueAsString(request);
            logger.info("Request body JSON: {}", jsonBody);
            
            HttpEntity<WebhookSubmitRequest> entity = new HttpEntity<>(request, headers);
            
            try {
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
            } catch (HttpClientErrorException.Unauthorized e) {
                logger.error("401 Unauthorized - Authentication failed");
                logger.error("Response body: {}", e.getResponseBodyAsString());
                logger.error("Response headers: {}", e.getResponseHeaders());
                logger.error("Access token length: {}", token != null ? token.length() : "null");
                logger.error("Authorization header value: {}...", token != null && token.length() > 30 ? token.substring(0, 30) : token);
                
                // Try with Bearer prefix as fallback (in case API expects it)
                logger.info("Retrying with Bearer prefix...");
                headers.set("Authorization", "Bearer " + token);
                HttpEntity<WebhookSubmitRequest> retryEntity = new HttpEntity<>(request, headers);
                
                try {
                    ResponseEntity<String> retryResponse = restTemplate.exchange(
                        webhookUrl,
                        HttpMethod.POST,
                        retryEntity,
                        String.class
                    );
                    
                    if (retryResponse.getStatusCode().is2xxSuccessful()) {
                        logger.info("Solution submitted successfully without Bearer prefix! Response: {}", retryResponse.getBody());
                    } else {
                        logger.error("Still failed without Bearer prefix. Status: {}, Response: {}", 
                            retryResponse.getStatusCode(), retryResponse.getBody());
                    }
                } catch (Exception retryException) {
                    logger.error("Retry without Bearer prefix also failed", retryException);
                }
            } catch (HttpClientErrorException e) {
                logger.error("Client error (4xx) while submitting solution. Status: {}", e.getStatusCode());
                logger.error("Response body: {}", e.getResponseBodyAsString());
                logger.error("Response headers: {}", e.getResponseHeaders());
            } catch (HttpServerErrorException e) {
                logger.error("Server error (5xx) while submitting solution. Status: {}", e.getStatusCode());
                logger.error("Response body: {}", e.getResponseBodyAsString());
            }
            
        } catch (Exception e) {
            logger.error("Exception while submitting solution", e);
        }
    }
}

