package com.hopon.adapter.entur;

import com.hopon.core.DepartureFetcher;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.io.IOException;

public class EnturClient implements DepartureFetcher {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String endpoint = "https://api.entur.io/journey-planner/v3/graphql";

    @Override
    public String getDepartures(String stopPlaceId) {
        // Lager GraphQL-spørring
        String query = "{ stopPlace(id: \"" + stopPlaceId + "\") { name, estimatedCalls { expectedDepartureTime } } }";
        String body = "{\"query\": \"" + query + "\"}";

        try {
            return fetchDepartures(body);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String fetchDepartures(String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(endpoint))
            .header("Content-Type", "application/json")
            .header("ET-Client-Name", "hopon-app") // Entur krever klientnavn
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}


