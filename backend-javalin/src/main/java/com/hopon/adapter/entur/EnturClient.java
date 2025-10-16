package com.hopon.adapter.entur;

import com.hopon.Core.ports.DepartureFetcher;
import com.hopon.Core.model.Departure;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnturClient implements DepartureFetcher {

    private static final String ENTUR_URL = "https://api.entur.io/journey-planner/v3/graphql";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<Departure> fetchDepartures(String stopPlaceId) {
        try {
            //  GraphQL-query på én linje
            String query = String.format(
                "{ stopPlace(id: \"%s\") { name estimatedCalls(timeRange: 7200, numberOfDepartures: 5) { expectedDepartureTime destinationDisplay { frontText } serviceJourney { line { publicCode } } } } }",
                stopPlaceId
            );

            String body = "{\"query\":\"" + query.replace("\"", "\\\"") + "\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ENTUR_URL))
                    .header("Content-Type", "application/json")
                    .header("ET-Client-Name", "hopon-student-sami")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(" FULL ENTUR-RESPONS: " + response.body());

            return parseDepartures(response.body());

        } catch (Exception e) {
            System.err.println(" Feil ved henting av Entur-data: " + e.getMessage());
            return List.of();
        }
    }

    private List<Departure> parseDepartures(String json) {
        List<Departure> departures = new ArrayList<>();

        try {
            JsonNode root = mapper.readTree(json);
            JsonNode stopPlace = root.path("data").path("stopPlace");

            if (stopPlace.isMissingNode()) {
                System.err.println(" Ingen stopPlace funnet i responsen!");
                return departures;
            }

            String fromName = stopPlace.path("name").asText("Ukjent sted");
            JsonNode calls = stopPlace.path("estimatedCalls");

            if (!calls.isArray()) {
                System.err.println(" Ingen estimatedCalls-liste funnet!");
                return departures;
            }

            for (JsonNode call : calls) {
                String line = call.path("serviceJourney").path("line").path("publicCode").asText("");
                String to = call.path("destinationDisplay").path("frontText").asText("");
                String time = call.path("expectedDepartureTime").asText("");

                if (line.isEmpty() || to.isEmpty() || time.isEmpty()) continue;

                LocalDateTime departureTime = OffsetDateTime.parse(time)
                        .toLocalDateTime();

                departures.add(new Departure(fromName, to, line, departureTime));

            }

            if (departures.isEmpty()) {
                departures.add(new Departure("Ukjent", "Ingen avganger", "L0", LocalDateTime.now()));
                System.out.println(" Ingen reelle avganger – viser dummy-data i stedet.");
            }

        } catch (Exception e) {
            System.err.println("Feil ved parsing av Entur-data: " + e.getMessage());
        }

        return departures;
    }
}
