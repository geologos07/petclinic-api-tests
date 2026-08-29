package com.example.petclinicapitests.dto;

public record VisitResponse(
    String date,
    String description,
    Integer id,
    Integer petId
) {
}
