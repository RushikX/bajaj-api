package com.bajaj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebhookGenerateResponse {
    
    @JsonProperty("webhook")
    private String webhook;
    
    @JsonProperty("accessToken")
    private String accessToken;
    
    // Additional fields that might be in the response
    private String question;
    private Object data;

    public WebhookGenerateResponse() {
    }

    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

