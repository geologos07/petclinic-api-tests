package com.example.petclinicapitests;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;

abstract class OwnerBaseTest extends BaseApiTest {

    protected Integer createdOwnerId;

    @AfterEach
    void deleteCreatedOwnerIfNecessary() {
        if (createdOwnerId == null) {
            return;
        }

        Response existingOwner = ownerClient.get(createdOwnerId);
        if (existingOwner.statusCode() != 404) {
            ownerClient.delete(createdOwnerId);
        }
        createdOwnerId = null;
    }
}
