package com.example.petclinicapitests.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProblemDetailResponse(
    String type,
    String title,
    Integer status,
    String detail,
    String timestamp,
    List<ValidationMessageResponse> schemaValidationErrors
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ValidationMessageResponse(String message) {
    }
}
