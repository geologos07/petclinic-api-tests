package com.example.petclinicapitests.config;

import io.qameta.allure.Allure;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.util.Objects;

public class AllureHttpLoggingFilter implements Filter {

    @Override
    public Response filter(
        FilterableRequestSpecification requestSpecification,
        FilterableResponseSpecification responseSpecification,
        FilterContext filterContext
    ) {
        Allure.addAttachment("HTTP request", "text/plain", requestDescription(requestSpecification));

        var response = filterContext.next(requestSpecification, responseSpecification);

        Allure.addAttachment("HTTP response", "text/plain", responseDescription(response));
        return response;
    }

    private String requestDescription(FilterableRequestSpecification requestSpecification) {
        return "Method: " + requestSpecification.getMethod()
            + "\nURI: " + requestSpecification.getURI()
            + "\nHeaders: " + requestSpecification.getHeaders()
            + "\nBody: " + Objects.toString(requestSpecification.getBody(), "");
    }

    private String responseDescription(Response response) {
        return "Status: " + response.getStatusLine()
            + "\nHeaders: " + response.getHeaders()
            + "\nBody: " + response.getBody().asPrettyString();
    }
}
