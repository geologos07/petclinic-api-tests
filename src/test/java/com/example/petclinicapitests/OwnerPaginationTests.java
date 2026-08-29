package com.example.petclinicapitests;

import com.example.petclinicapitests.assertion.ProblemDetailAssertions;
import com.example.petclinicapitests.dto.OwnerPageResponse;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Owners")
@Story("Пагинация списка владельцев")
@Severity(SeverityLevel.NORMAL)
class OwnerPaginationTests extends OwnerBaseTest {

    @ParameterizedTest(name = "page={0}, size={1}")
    @CsvSource({"0, 1", "0, 5", "0, 100"})
    @DisplayName("GET /api/v2/owners принимает корректные параметры пагинации")
    void listOwnersPageShouldSupportValidPagination(int page, int size) {
        var result = ownerClient.listPage(page, size)
            .then()
            .statusCode(200)
            .extract()
            .as(OwnerPageResponse.class);

        assertThat(result.page()).isEqualTo(page);
        assertThat(result.size()).isEqualTo(size);
        assertThat(result.content()).isNotNull();
        assertThat(result.totalElements()).isGreaterThanOrEqualTo(0L);
        assertThat(result.totalPages()).isGreaterThanOrEqualTo(0);
    }

    @ParameterizedTest(name = "page={0}, size={1}")
    @CsvSource({"-1, 20", "0, 0", "0, 101"})
    @Tag("PetBug-3")
    @Issue("PetBug-3")
    @DisplayName("GET /api/v2/owners отклоняет некорректные параметры пагинации")
    void listOwnersPageShouldRejectInvalidPagination(int page, int size) {
        // Swagger описывает эти значения как 400, поэтому падение теста фиксирует дефект API.
        var response = ownerClient.listPage(page, size);

        ProblemDetailAssertions.assertProblemDetail(response, 400, null);
    }
}
