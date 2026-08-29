package com.example.petclinicapitests.service;

import com.example.petclinicapitests.dto.OwnerRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Service;

import static io.restassured.RestAssured.given;

@Service
public class OwnerClient {

    private final RequestSpecification requestSpecification;

    public OwnerClient(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
    }

    @Step("Создание владельца")
    public Response create(OwnerRequest ownerRequest) {
        return given()
            .spec(requestSpecification)
            .body(ownerRequest)
            .when()
            .post("/owners");
    }

    @Step("Получение владельца с идентификатором {ownerId}")
    public Response get(int ownerId) {
        return given()
            .spec(requestSpecification)
            .pathParam("ownerId", ownerId)
            .when()
            .get("/owners/{ownerId}");
    }

    @Step("Обновление владельца с идентификатором {ownerId}")
    public Response update(int ownerId, OwnerRequest ownerRequest) {
        return given()
            .spec(requestSpecification)
            .pathParam("ownerId", ownerId)
            .body(ownerRequest)
            .when()
            .put("/owners/{ownerId}");
    }

    @Step("Удаление владельца с идентификатором {ownerId}")
    public Response delete(int ownerId) {
        return given()
            .spec(requestSpecification)
            .pathParam("ownerId", ownerId)
            .when()
            .delete("/owners/{ownerId}");
    }
}
