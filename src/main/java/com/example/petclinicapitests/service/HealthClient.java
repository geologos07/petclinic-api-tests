package com.example.petclinicapitests.service;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Service;

import static io.restassured.RestAssured.given;

@Service
public class HealthClient {

    private final RequestSpecification requestSpecification;

    public HealthClient(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
    }

    @Step("Проверка состояния приложения через actuator health")
    public Response get() {
        return given()
            .spec(requestSpecification)
            .when()
            .get("/actuator/health");
    }
}
