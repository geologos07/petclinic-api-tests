package com.example.petclinicapitests.dto;

import java.util.List;

public record OwnerResponse(
    String firstName,
    String lastName,
    String address,
    String city,
    String telephone,
    Integer id,
    List<PetResponse> pets
) {
}
