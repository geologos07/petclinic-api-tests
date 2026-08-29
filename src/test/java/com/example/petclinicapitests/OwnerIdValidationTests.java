package com.example.petclinicapitests;

import com.example.petclinicapitests.assertion.ProblemDetailAssertions;
import com.example.petclinicapitests.data.OwnerTestData;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@Feature("Owners")
@Story("Проверка идентификатора владельца")
@Severity(SeverityLevel.NORMAL)
class OwnerIdValidationTests extends OwnerBaseTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("negativeOwnerIds")
    @Tag("PetBug-2")
    @Issue("PetBug-2")
    @DisplayName("GET /api/owners/{ownerId} для отрицательного идентификатора возвращает 400")
    void getOwnerWithNegativeIdShouldReturnBadRequest(String caseName, int ownerId, int expectedStatus) {
        ProblemDetailAssertions.assertStatus(ownerClient.get(ownerId), expectedStatus);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("notFoundOwnerIds")
    @DisplayName("GET /api/owners/{ownerId} для неизвестного идентификатора возвращает 404")
    void getUnknownOwnerShouldReturnNotFound(String caseName, int ownerId, int expectedStatus) {
        ProblemDetailAssertions.assertStatus(ownerClient.get(ownerId), expectedStatus);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("negativeOwnerIds")
    @Tag("PetBug-2")
    @Issue("PetBug-2")
    @DisplayName("PUT /api/owners/{ownerId} для отрицательного идентификатора возвращает 400")
    void updateOwnerWithNegativeIdShouldReturnBadRequest(String caseName, int ownerId, int expectedStatus) {
        ProblemDetailAssertions.assertStatus(
            ownerClient.update(ownerId, OwnerTestData.uniqueOwner()),
            expectedStatus
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("notFoundOwnerIds")
    @DisplayName("PUT /api/owners/{ownerId} для неизвестного идентификатора возвращает 404")
    void updateUnknownOwnerShouldReturnNotFound(String caseName, int ownerId, int expectedStatus) {
        ProblemDetailAssertions.assertStatus(
            ownerClient.update(ownerId, OwnerTestData.uniqueOwner()),
            expectedStatus
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("negativeOwnerIds")
    @Tag("PetBug-2")
    @Issue("PetBug-2")
    @DisplayName("DELETE /api/owners/{ownerId} для отрицательного идентификатора возвращает 400")
    void deleteOwnerWithNegativeIdShouldReturnBadRequest(String caseName, int ownerId, int expectedStatus) {
        ProblemDetailAssertions.assertStatus(ownerClient.delete(ownerId), expectedStatus);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("notFoundOwnerIds")
    @DisplayName("DELETE /api/owners/{ownerId} для неизвестного идентификатора возвращает 404")
    void deleteUnknownOwnerShouldReturnNotFound(String caseName, int ownerId, int expectedStatus) {
        ProblemDetailAssertions.assertStatus(ownerClient.delete(ownerId), expectedStatus);
    }

    private static Stream<Arguments> negativeOwnerIds() {
        // Swagger описывает отрицательный ID как 400, но текущая версия Petclinic отвечает 500.
        return Stream.of(arguments("отрицательный идентификатор", -1, 400));
    }

    private static Stream<Arguments> notFoundOwnerIds() {
        return Stream.of(arguments("несуществующий идентификатор", 999999, 404));
    }
}
