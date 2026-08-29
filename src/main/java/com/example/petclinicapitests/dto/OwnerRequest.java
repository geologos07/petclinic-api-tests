package com.example.petclinicapitests.dto;

public record OwnerRequest(
    String firstName,
    String lastName,
    String address,
    String city,
    String telephone
) {
}
