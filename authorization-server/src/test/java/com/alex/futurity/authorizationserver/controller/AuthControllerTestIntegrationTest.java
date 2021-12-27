package com.alex.futurity.authorizationserver.controller;

import com.alex.futurity.authorizationserver.AuthorizationServerApplication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AuthorizationServerApplication.class)
@ExtendWith({SpringExtension.class})
class AuthControllerTestIntegrationTest {

}