package com.hopon.adapter.entur;

import com.hopon.core.ports.DepartureFetcher;
import com.hopon.core.model.Departure;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EnturClient implements DepartureFetcher {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String endpoint = "https://api.entur.io/journey-planner/v3/graphql";

    @Override
    public List<Departure> fetchDepartures(String stopPlaceId) {
        String query = "{ stopPlace(id: \"" + stopPlaceId + "\") { name, estimatedCalls { expectedDepartureTime destinationDisplay { frontText } serviceJourney { line { publicCode } } } } }";
        String body = "{\"query\": \"" + query.replace("\"", "\\\"") + "\"}";

        try {
            String json = sendRequest(body);

            // 👉 foreløpig dummy-parsing (du kan bruke JSON-lib senere)
            List<Departure> departures = new ArrayList<>();
            departures.add(new Departure("L1", "Dummy-destination", LocalDateTime.now().plusMinutes(5)));
            return departures;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private String sendRequest(String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .header("ET-Client-Name", "hopon-app")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
