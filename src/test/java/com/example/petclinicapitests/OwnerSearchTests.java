package com.example.petclinicapitests;

import com.example.petclinicapitests.assertion.OwnerAssertions;
import com.example.petclinicapitests.data.OwnerTestData;
import com.example.petclinicapitests.dto.OwnerResponse;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Owners")
@Story("Поиск владельцев")
@Severity(SeverityLevel.NORMAL)
class OwnerSearchTests extends OwnerBaseTest {

    @Test
    @DisplayName("GET /api/owners возвращает список владельцев")
    void listOwnersShouldReturnArray() {
        var owner = OwnerTestData.uniqueOwner();
        var created = ownerClient.create(owner)
            .then()
            .statusCode(201)
            .extract()
            .as(OwnerResponse.class);
        createdOwnerId = created.id();

        var owners = ownerClient.list()
            .then()
            .statusCode(200)
            .extract()
            .as(OwnerResponse[].class);

        assertThat(owners).isNotNull().isNotEmpty();
        var foundOwner = Arrays.stream(owners)
            .filter(item -> item.id().equals(createdOwnerId))
            .findFirst()
            .orElseThrow();

        OwnerAssertions.assertOwnerMapping(foundOwner, owner);
    }

    @Test
    @DisplayName("GET /api/owners фильтрует владельцев по фамилии")
    void listOwnersShouldFilterByLastName() {
        var owner = OwnerTestData.uniqueOwner();
        var created = ownerClient.create(owner)
            .then()
            .statusCode(201)
            .extract()
            .as(OwnerResponse.class);
        createdOwnerId = created.id();

        var owners = ownerClient.list(owner.lastName())
            .then()
            .statusCode(200)
            .extract()
            .as(OwnerResponse[].class);

        var foundOwner = Arrays.stream(owners)
            .filter(item -> item.id().equals(createdOwnerId))
            .findFirst()
            .orElseThrow();

        OwnerAssertions.assertOwnerMapping(foundOwner, owner);
    }
}
