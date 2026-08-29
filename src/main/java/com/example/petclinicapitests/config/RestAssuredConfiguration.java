package com.example.petclinicapitests.config;

import io.restassured.builder.RequestSpecBuilder;
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
            .build();
    }
}
