package com.bajaj.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    
    private Webhook webhook = new Webhook();
    private User user = new User();

    public Webhook getWebhook() {
        return webhook;
    }

    public void setWebhook(Webhook webhook) {
        this.webhook = webhook;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class Webhook {
        private String generateUrl;
        private String submitUrl;

        public String getGenerateUrl() {
            return generateUrl;
        }

        public void setGenerateUrl(String generateUrl) {
            this.generateUrl = generateUrl;
        }

        public String getSubmitUrl() {
            return submitUrl;
        }

        public void setSubmitUrl(String submitUrl) {
            this.submitUrl = submitUrl;
        }
    }

    public static class User {
        private String name;
        private String regNo;
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRegNo() {
            return regNo;
        }

        public void setRegNo(String regNo) {
            this.regNo = regNo;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}

