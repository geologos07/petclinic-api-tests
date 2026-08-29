package com.example.petclinicapitests.dto;

import java.util.List;

public record HealthResponse(
    List<String> groups,
    String status
) {
}
