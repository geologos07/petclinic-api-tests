package com.example.petclinicapitests.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestAssuredConfiguration {

    @Bean
    public RequestSpecification requestSpecification(
        @Value("${petclinic.base-url}") String baseUrl
    ) {
        return new RequestSpecBuilder()
            .setBaseUri(baseUrl)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .setConfig(RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                    .setParam("http.connection.timeout", 5_000)
                    .setParam("http.socket.timeout", 10_000)
                    .setParam("http.connection-manager.timeout", 5_000)))
            .addFilter(new AllureHttpLoggingFilter())
            .build();
    }
}
