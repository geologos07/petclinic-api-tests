package com.example.petclinicapitests;

import com.example.petclinicapitests.assertion.OwnerAssertions;
import com.example.petclinicapitests.assertion.ProblemDetailAssertions;
import com.example.petclinicapitests.data.OwnerTestData;
import com.example.petclinicapitests.dto.OwnerResponse;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Owners")
@Story("Owner CRUD")
@Severity(SeverityLevel.CRITICAL)
class OwnerCrudTests extends OwnerBaseTest {

    @Test
    @DisplayName("Создание, получение, обновление и удаление владельца")
    void ownerCrudFlow() {
        var ownerToCreate = OwnerTestData.uniqueOwner();
        var created = ownerClient.create(ownerToCreate)
            .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .extract()
            .as(OwnerResponse.class);
        createdOwnerId = created.id();

        OwnerAssertions.assertOwnerMapping(created, ownerToCreate);

        var received = ownerClient.get(createdOwnerId)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract()
            .as(OwnerResponse.class);
        OwnerAssertions.assertOwnerMapping(received, ownerToCreate);
        OwnerAssertions.assertOwnerId(received, createdOwnerId);

        var ownerToUpdate = OwnerTestData.updatedOwner();
        var updateResponse = ownerClient.update(createdOwnerId, ownerToUpdate);
        assertThat(updateResponse.statusCode()).isEqualTo(204);
        assertThat(updateResponse.asString()).as("PUT не должен возвращать тело при статусе 204").isBlank();

        var updated = ownerClient.get(createdOwnerId)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract()
            .as(OwnerResponse.class);

        OwnerAssertions.assertOwnerMapping(updated, ownerToUpdate);
        OwnerAssertions.assertOwnerId(updated, createdOwnerId);

        var deleteResponse = ownerClient.delete(createdOwnerId);
        assertThat(deleteResponse.statusCode()).isEqualTo(204);
        assertThat(deleteResponse.asString()).as("DELETE не должен возвращать тело при статусе 204").isBlank();

        var afterDelete = ownerClient.get(createdOwnerId);
        ProblemDetailAssertions.assertStatus(afterDelete, 404);
        createdOwnerId = null;
    }
}
