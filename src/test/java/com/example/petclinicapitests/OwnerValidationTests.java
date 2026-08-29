package com.example.petclinicapitests;

import com.example.petclinicapitests.assertion.ProblemDetailAssertions;
import com.example.petclinicapitests.data.OwnerTestData;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@Feature("Owners")
@Story("Валидация создания владельца")
@Severity(SeverityLevel.NORMAL)
class OwnerValidationTests extends BaseApiTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidOwnerBodies")
    void invalidOwnerShouldReturnBadRequest(String caseName, Map<String, Object> body, String expectedField) {
        var response = ownerClient.create(body);

        ProblemDetailAssertions.assertProblemDetail(response, 400, expectedField);
    }

    private static Stream<Arguments> invalidOwnerBodies() {
        return Stream.of(
            arguments("firstName отсутствует", OwnerTestData.withoutField("firstName"), "firstName"),
            arguments("lastName отсутствует", OwnerTestData.withoutField("lastName"), "lastName"),
            arguments("address отсутствует", OwnerTestData.withoutField("address"), "address"),
            arguments("city отсутствует", OwnerTestData.withoutField("city"), "city"),
            arguments("telephone отсутствует", OwnerTestData.withoutField("telephone"), "telephone"),
            arguments("firstName равен null", OwnerTestData.withField("firstName", null), "firstName"),
            arguments("lastName пустой", OwnerTestData.withField("lastName", ""), "lastName"),
            arguments("address пустой", OwnerTestData.withField("address", ""), "address"),
            arguments("city пустой", OwnerTestData.withField("city", ""), "city"),
            arguments("telephone пустой", OwnerTestData.withField("telephone", ""), "telephone"),
            arguments("firstName длиннее 30 символов", OwnerTestData.withField("firstName", "A".repeat(31)), "firstName"),
            arguments("lastName длиннее 30 символов", OwnerTestData.withField("lastName", "A".repeat(31)), "lastName"),
            arguments("address длиннее 255 символов", OwnerTestData.withField("address", "A".repeat(256)), "address"),
            arguments("city длиннее 80 символов", OwnerTestData.withField("city", "A".repeat(81)), "city"),
            arguments("firstName содержит цифры", OwnerTestData.withField("firstName", "John2"), "firstName"),
            arguments("lastName содержит цифры", OwnerTestData.withField("lastName", "Owner2"), "lastName"),
            arguments("telephone содержит буквы", OwnerTestData.withField("telephone", "12345abc"), "telephone"),
            arguments("telephone длиннее 20 символов", OwnerTestData.withField("telephone", "1".repeat(21)), "telephone")
        );
    }
}
