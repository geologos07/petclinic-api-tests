package com.example.petclinicapitests.dto;

import java.util.List;

public record OwnerPageResponse(
    List<OwnerResponse> content,
    Integer page,
    Integer size,
    Long totalElements,
    Integer totalPages
) {
}
