package com.example.petclinicapitests;

import com.example.petclinicapitests.service.OwnerClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest(classes = PetclinicTestApplication.class)
abstract class BaseApiTest {

    @Autowired
    protected OwnerClient ownerClient;
}
