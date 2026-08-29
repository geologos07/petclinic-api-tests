package com.example.petclinicapitests.dto;

import java.util.List;

public record PetResponse(
    String name,
    String birthDate,
    PetTypeResponse type,
    Integer id,
    Integer ownerId,
    List<VisitResponse> visits
) {
}
