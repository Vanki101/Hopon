/*package com.hopon.pp;

import kong.unirest.Unirest;
import kong.unirest.HttpResponse;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class DepartureEndpointTest {

    private static io.javalin.Javalin app;

    @BeforeAll
    static void startServer() {
        app = TestApp.startTestServer();
    }

    @AfterAll
    static void stopServer() {
        app.stop();
    }

    @Test
    void testDeparturesEndpoint() {
        HttpResponse<String> response = Unirest.get("http://localhost:7070/departures?id=123").asString();

        assertEquals(200, response.getStatus(), "Server should respond 200 OK");
        assertTrue(response.getBody().contains("Majorstuen"), "Response should contain mock data");
    }
}
*/