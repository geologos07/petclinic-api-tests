package com.example.petclinicapitests.service;

import com.example.petclinicapitests.dto.OwnerRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Service;

import java.util.Map;

import static io.restassured.RestAssured.given;

@Service
public class OwnerClient {

    private final RequestSpecification requestSpecification;

    public OwnerClient(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
    }

    @Step("Создание владельца")
    public Response create(OwnerRequest ownerRequest) {
        return createRequest(ownerRequest);
    }

    @Step("Создание владельца с произвольным телом запроса")
    public Response create(Map<String, Object> ownerRequest) {
        return createRequest(ownerRequest);
    }

    private Response createRequest(Object ownerRequest) {
        return given()
            .spec(requestSpecification)
            .body(ownerRequest)
            .when()
            .post("/api/owners");
    }

    @Step("Получение владельца с идентификатором {ownerId}")
    public Response get(int ownerId) {
        return given()
            .spec(requestSpecification)
            .pathParam("ownerId", ownerId)
            .when()
            .get("/api/owners/{ownerId}");
    }

    @Step("Обновление владельца с идентификатором {ownerId}")
    public Response update(int ownerId, OwnerRequest ownerRequest) {
        return given()
            .spec(requestSpecification)
            .pathParam("ownerId", ownerId)
            .body(ownerRequest)
            .when()
            .put("/api/owners/{ownerId}");
    }

    @Step("Удаление владельца с идентификатором {ownerId}")
    public Response delete(int ownerId) {
        return given()
            .spec(requestSpecification)
            .pathParam("ownerId", ownerId)
            .when()
            .delete("/api/owners/{ownerId}");
    }

    @Step("Получение списка владельцев")
    public Response list() {
        return list(null);
    }

    @Step("Получение списка владельцев с фильтром по фамилии")
    public Response list(String lastName) {
        var request = given().spec(requestSpecification);
        if (lastName != null) {
            request.queryParam("lastName", lastName);
        }
        return request.when().get("/api/owners");
    }

    @Step("Получение страницы владельцев")
    public Response listPage(Integer page, Integer size) {
        return listPage(null, page, size);
    }

    @Step("Получение страницы владельцев с фильтром и пагинацией")
    public Response listPage(String lastName, Integer page, Integer size) {
        var request = given().spec(requestSpecification);
        if (lastName != null) {
            request.queryParam("lastName", lastName);
        }
        if (page != null) {
            request.queryParam("page", page);
        }
        if (size != null) {
            request.queryParam("size", size);
        }
        return request.when().get("/api/v2/owners");
    }
}
