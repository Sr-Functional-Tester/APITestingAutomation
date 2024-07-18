package com.apitest;

public class TestCase {
    String id;
    String description;
    String operation;
    String endpoint;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getAssertions() {
        return assertions;
    }

    public void setAssertions(String assertions) {
        this.assertions = assertions;
    }

    String contentType;
    String token;
    String requestBody;
    String assertions;

    TestCase(String id, String description, String operation, String endpoint, String contentType, String token, String requestBody, String assertions) {
        this.id = id;
        this.description = description;
        this.operation = operation;
        this.endpoint = endpoint;
        this.contentType = contentType;
        this.token = token;
        this.requestBody = requestBody;
        this.assertions = assertions;
    }
}
