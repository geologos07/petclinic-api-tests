package com.example.petclinicapitests.assertion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.petclinicapitests.dto.ProblemDetailResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Collection;
import java.util.Set;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public final class ProblemDetailAssertions {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ProblemDetailAssertions() {
    }

    @Step("Проверка HTTP-статуса ответа с ошибкой")
    public static void assertStatus(Response response, int expectedStatus) {
        assertThat(response.statusCode()).isEqualTo(expectedStatus);
    }

    public static void assertProblemDetail(Response response, int expectedStatus, String expectedField) {
        assertProblemDetail(response, Set.of(expectedStatus), expectedField);
    }

    public static void assertProblemDetail(
        Response response,
        Collection<Integer> expectedStatuses,
        String expectedField
    ) {
        assertThat(expectedStatuses).contains(response.statusCode());
        var body = response.asString();
        assertThat(body).as("Ответ с ошибкой должен содержать ProblemDetail").isNotBlank();

        ProblemDetailResponse problem;
        try {
            problem = OBJECT_MAPPER.readValue(body, ProblemDetailResponse.class);
        } catch (JsonProcessingException exception) {
            throw new AssertionError("Ответ с ошибкой не удалось преобразовать в ProblemDetail: " + body, exception);
        }

        assertAll(
            () -> step("Проверяем HTTP-статус ошибки", () ->
                assertThat(expectedStatuses).contains(response.statusCode())),
            () -> step("Проверяем status в ProblemDetail", () ->
                assertThat(problem.status()).isEqualTo(response.statusCode())),
            () -> step("Проверяем title ошибки", () ->
                assertThat(problem.title()).as("title должен быть заполнен").isNotBlank()),
            () -> step("Проверяем detail ошибки", () ->
                assertThat(problem.detail()).as("detail должен быть заполнен").isNotBlank()),
            () -> step("Проверяем timestamp ошибки", () ->
                assertThat(problem.timestamp()).as("timestamp должен быть заполнен").isNotBlank()),
            () -> step("Проверяем список schemaValidationErrors", () ->
                assertThat(problem.schemaValidationErrors()).as("список ошибок должен присутствовать")
                    .isNotNull())
        );

        if (expectedField != null) {
            assertThat(problem.schemaValidationErrors())
                .anySatisfy(error -> assertThat(error.message()).contains(expectedField));
        }
    }
}
