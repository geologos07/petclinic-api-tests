package com.example.petclinicapitests;

import com.example.petclinicapitests.assertion.ProblemDetailAssertions;
import com.example.petclinicapitests.data.OwnerTestData;
import com.example.petclinicapitests.dto.OwnerResponse;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

@Feature("Owners")
@Story("Уникальность телефона владельца")
@Severity(SeverityLevel.NORMAL)
class OwnerDuplicateTests extends OwnerBaseTest {

    @Test
    @Tag("PetBug-1")
    @Issue("PetBug-1")
    @DisplayName("Нельзя создать двух владельцев с одним номером телефона")
    void duplicateTelephoneShouldBeRejected() {
        var firstOwner = OwnerTestData.uniqueOwner();
        var first = ownerClient.create(firstOwner)
            .then()
            .statusCode(201)
            .extract()
            .as(OwnerResponse.class);
        createdOwnerId = first.id();

        var duplicateOwner = OwnerTestData.withTelephone(
            OwnerTestData.uniqueOwner(),
            firstOwner.telephone()
        );
        var duplicateResponse = ownerClient.create(duplicateOwner);
        Integer duplicateOwnerId = null;

        try {
            // Swagger не фиксирует конкретный статус для конфликта, поэтому допустимы 400 и 409.
            ProblemDetailAssertions.assertProblemDetail(
                duplicateResponse,
                Set.of(400, 409),
                "telephone"
            );
        } finally {
            if (duplicateResponse.statusCode() == 201 && !duplicateResponse.asString().isBlank()) {
                duplicateOwnerId = duplicateResponse.as(OwnerResponse.class).id();
            }
            if (duplicateOwnerId != null) {
                ownerClient.delete(duplicateOwnerId);
            }
        }
    }
}
