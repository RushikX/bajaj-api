package com.bajaj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebhookSubmitRequest {
    
    @JsonProperty("finalQuery")
    private String finalQuery;

    public WebhookSubmitRequest() {
    }

    public WebhookSubmitRequest(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    public String getFinalQuery() {
        return finalQuery;
    }

    public void setFinalQuery(String finalQuery) {
        this.finalQuery = finalQuery;
    }
}

