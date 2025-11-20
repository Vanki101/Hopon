package com.hopon.app;

import kong.unirest.Unirest;
import kong.unirest.HttpResponse;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class DepartureEndpointTest {

    private static io.javalin.Javalin app;

    @BeforeAll
    static void startServer() {
        // Bruk TestApp som allerede har mock data!
        app = TestApp.startTestServer();
    }

    @AfterAll
    static void stopServer() {
        if (app != null) {
            app.stop();
        }
    }

    @Test
    void testDeparturesEndpoint() {
        // Kall test serveren på port 7070
        HttpResponse<String> response = Unirest.get("http://localhost:7070/departures?id=123").asString();

        // Debug output
        System.out.println("Response status: " + response.getStatus());
        System.out.println("Response body: " + response.getBody());

        assertEquals(200, response.getStatus(), "Server should respond 200 OK");

        // Sjekk at responsen inneholder data fra MockEnturClient
        assertNotNull(response.getBody(), "Response body should not be null");
        assertFalse(response.getBody().isEmpty(), "Response body should not be empty");
    }
}