package com.example.petclinicapitests.data;

import com.example.petclinicapitests.dto.OwnerRequest;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public final class OwnerTestData {

    private OwnerTestData() {
    }

    public static OwnerRequest uniqueOwner() {
        var suffix = letters(6);
        return new OwnerRequest(
            "John" + suffix,
            "Owner" + suffix,
            "Automation address " + suffix,
            "TestCity" + suffix,
            randomTelephone()
        );
    }

    public static OwnerRequest updatedOwner() {
        var suffix = letters(6);
        return new OwnerRequest(
            "Updated" + suffix,
            "Changed" + suffix,
            "Updated address " + suffix,
            "UpdatedCity" + suffix,
            randomTelephone()
        );
    }

    public static OwnerRequest withTelephone(OwnerRequest owner, String telephone) {
        return new OwnerRequest(
            owner.firstName(),
            owner.lastName(),
            owner.address(),
            owner.city(),
            telephone
        );
    }

    public static Map<String, Object> validOwnerBody() {
        var owner = uniqueOwner();
        return new LinkedHashMap<>(Map.of(
            "firstName", owner.firstName(),
            "lastName", owner.lastName(),
            "address", owner.address(),
            "city", owner.city(),
            "telephone", owner.telephone()
        ));
    }

    public static Map<String, Object> withField(String field, Object value) {
        var body = validOwnerBody();
        body.put(field, value);
        return body;
    }

    public static Map<String, Object> withoutField(String field) {
        var body = validOwnerBody();
        body.remove(field);
        return body;
    }

    private static String letters(int length) {
        return ThreadLocalRandom.current()
            .ints(length, 0, 26)
            .mapToObj(value -> String.valueOf((char) ('A' + value)))
            .collect(Collectors.joining());
    }

    private static String randomTelephone() {
        return String.valueOf(ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L));
    }
}
