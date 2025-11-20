package com.hopon.app;

import kong.unirest.Unirest;
import org.junit.jupiter.api.*;

public class AppIntegrationTest {

    @BeforeAll
    static void setup() {
        App.main(new String[]{}); // Starter serveren
    }

    @AfterAll
    static void tearDown() {
        Unirest.shutDown();
    }

    @Test
    void testServerResponds() {
        var response = Unirest.get("http://localhost:8080/health").asString();
        Assertions.assertEquals(200, response.getStatus());
    }
}
