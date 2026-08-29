package com.example.petclinicapitests;

import com.example.petclinicapitests.dto.HealthResponse;
import com.example.petclinicapitests.service.HealthClient;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Health check")
@Story("Проверка доступности приложения")
@Severity(SeverityLevel.BLOCKER)
class HealthApiTests extends BaseApiTest {

    @Autowired
    private HealthClient healthClient;

    @Test
    @DisplayName("GET /actuator/health возвращает статус UP")
    void healthShouldBeUp() {
        var response = healthClient.get();
        var health = response.then()
            .statusCode(200)
            .extract()
            .as(HealthResponse.class);

        assertThat(health.status()).isEqualTo("UP");
    }
}
