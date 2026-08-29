package com.example.petclinicapitests.assertion;

import com.example.petclinicapitests.dto.OwnerRequest;
import com.example.petclinicapitests.dto.OwnerResponse;
import io.qameta.allure.Step;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public final class OwnerAssertions {

    private OwnerAssertions() {
    }

    @Step("Проверка маппинга ответа владельца")
    public static void assertOwnerMapping(OwnerResponse actual, OwnerRequest expected) {
        assertThat(actual).as("Ответ владельца не должен быть null").isNotNull();
        assertAll(
            () -> step("Проверяем идентификатор владельца", () ->
                assertThat(actual.id()).as("id владельца должен быть заполнен").isPositive()),
            () -> step("Проверяем поле firstName", () ->
                assertThat(actual.firstName()).as("firstName не соответствует запросу")
                    .isEqualTo(expected.firstName())),
            () -> step("Проверяем поле lastName", () ->
                assertThat(actual.lastName()).as("lastName не соответствует запросу")
                    .isEqualTo(expected.lastName())),
            () -> step("Проверяем поле address", () ->
                assertThat(actual.address()).as("address не соответствует запросу")
                    .isEqualTo(expected.address())),
            () -> step("Проверяем поле city", () ->
                assertThat(actual.city()).as("city не соответствует запросу")
                    .isEqualTo(expected.city())),
            () -> step("Проверяем поле telephone", () ->
                assertThat(actual.telephone()).as("telephone не соответствует запросу")
                    .isEqualTo(expected.telephone())),
            () -> step("Проверяем наличие массива pets", () ->
                assertThat(actual.pets()).as("pets должен присутствовать в ответе")
                    .isNotNull())
        );
    }

    @Step("Проверка идентификатора владельца")
    public static void assertOwnerId(OwnerResponse actual, Integer expectedId) {
        assertThat(actual.id())
            .as("Идентификатор владельца не соответствует ожидаемому")
            .isEqualTo(expectedId);
    }
}
