package com.hopon.adapter.entur;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnturLocationClient {

    private static final Logger log = LoggerFactory.getLogger(EnturLocationClient.class);
    private static final String URL = "https://api.entur.io/geocoder/v1/autocomplete?text=";

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public record GeoResult(String id, String name, double lon, double lat, List<String> categories) {}

    public List<GeoResult> findStopId(String name) {
        List<GeoResult> geoResults = new ArrayList<>();

        try {
            String encoded = URLEncoder.encode(name, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + encoded))
                    .header("ET-Client-Name", "hopon-student-sami")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.debug("Entur geocoder response for '{}': {}", name, response.body());

            JsonNode root = mapper.readTree(response.body());

            for (JsonNode feature : root.path("features")) {
                String id = feature.path("properties").path("id").asText();
                String label = feature.path("properties").path("label").asText();
                JsonNode categoriesNode = feature.path("properties").path("category");

                List<String> categories = new ArrayList<>();
                if (categoriesNode.isArray()) {
                    for (JsonNode cat : categoriesNode) {
                        categories.add(cat.asText());
                    }
                }

                JsonNode coords = feature.path("geometry").path("coordinates");
                double lon = coords.get(0).asDouble();
                double lat = coords.get(1).asDouble();

                log.debug("Found feature id={} name={} (lon={}, lat={})", id, label, lon, lat);

                geoResults.add(new GeoResult(id, label, lon, lat, categories));
            }

            if (geoResults.isEmpty()) {
                log.warn("No stopPlace found for '{}'", name);
            }

        } catch (Exception e) {
            log.error("Error during geocoder lookup for '{}': {}", name, e.getMessage(), e);
        }

        return geoResults;
    }

    public String findFirstStopPlaceId(String name) {
        var results = findStopId(name);

        if (results == null || results.isEmpty()) {
            log.warn("No geocoder results for '{}'", name);
            return null;
        }

        log.info("Geocoder returned {} results for '{}'", results.size(), name);

        for (GeoResult r : results) {
            log.debug("Candidate: id={} lon={} lat={}", r.id(), r.lon(), r.lat());
            if (r.id().startsWith("NSR:StopPlace")) {
                log.info("Found StopPlace ID for '{}': {}", name, r.id());
                return r.id();
            }
        }

        log.warn("No StopPlace ID found in geocoder results for '{}'", name);
        return null;
    }

    public GeoResult findFirstGeoResult(String name) {
        var results = findStopId(name);

        if (results == null || results.isEmpty()) {
            log.warn("No geocoder results for '{}'", name);
            return null;
        }

        GeoResult first = results.get(0);
        log.info("First result for '{}': {} (lat={}, lon={})",
                name, first.name(), first.lat(), first.lon());
        return first;
    }

    public String findNearestStopPlace(double lat, double lon) {
        try {
            var queryMap = java.util.Map.of(
                "query", String.format(java.util.Locale.US, """
                    {
                      nearest(
                        latitude: %.10f
                        longitude: %.10f
                        maximumDistance: 2000
                        filterByPlaceTypes: stopPlace
                        maximumResults: 1
                      ) {
                        edges {
                          node {
                            place {
                              ... on StopPlace {
                                id
                                name
                              }
                            }
                          }
                        }
                      }
                    }
                    """, lat, lon)
            );

            String body = mapper.writeValueAsString(queryMap);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.entur.io/journey-planner/v3/graphql"))
                    .header("Content-Type", "application/json")
                    .header("ET-Client-Name", "hopon-student-sami")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = mapper.readTree(response.body());
            JsonNode edges = root.path("data").path("nearest").path("edges");

            if (edges.isArray() && edges.size() > 0) {
                return edges.get(0).path("node").path("place").path("id").asText(null);
            }

        } catch (Exception e) {
            log.error("Error in findNearestStopPlace: {}", e.getMessage());
        }
        return null;
    }
}