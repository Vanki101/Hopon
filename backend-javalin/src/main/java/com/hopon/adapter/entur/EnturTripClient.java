package com.hopon.adapter.entur;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopon.core.model.TripPattern;
import com.hopon.core.model.Leg;
import com.hopon.core.model.Line;
import com.hopon.core.model.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class EnturTripClient {

    private static final Logger log = LoggerFactory.getLogger(EnturTripClient.class);
    private static final String ENTUR_URL = "https://api.entur.io/journey-planner/v3/graphql";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Planlegger reise mellom to StopPlace IDs
     * Returnerer liste av TripPattern (ikke bare legs)
     */
    public List<TripPattern> planTripByIds(String fromId, String toId) {
        try {
            String dateTime = ZonedDateTime.now()
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

            String query = String.format("""
                {
                  trip(
                    from: {
                      place: "%s"
                    }
                    to: {
                      place: "%s"
                    }
                    numTripPatterns: 3
                    dateTime: "%s"
                    walkSpeed: 1.3
                    arriveBy: false
                  ) {
                    tripPatterns {
                      expectedStartTime
                      expectedEndTime
                      duration
                      walkDistance
                      legs {
                        mode
                        distance
                        duration
                        expectedStartTime
                        expectedEndTime
                        line {
                          id
                          publicCode
                          transportMode
                        }
                        fromPlace {
                          name
                        }
                        toPlace {
                          name
                        }
                      }
                    }
                  }
                }
                """, fromId, toId, dateTime);

            String body = mapper.writeValueAsString(Map.of("query", query));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ENTUR_URL))
                    .header("Content-Type", "application/json")
                    .header("ET-Client-Name", "hopon-student-sami")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return parseTrips(response.body());

        } catch (Exception e) {
            log.error("Error fetching trip plan: {}", e.getMessage(), e);
            return List.of();
        }
    }

    private List<TripPattern> parseTrips(String json) {
        List<TripPattern> tripPatterns = new ArrayList<>();
        try {
            JsonNode root = mapper.readTree(json);
            
            JsonNode errors = root.path("errors");
            if (errors.isArray() && errors.size() > 0) {
                log.error("Entur API error: {}", errors.get(0).path("message").asText());
                return tripPatterns;
            }

            JsonNode patterns = root.path("data").path("trip").path("tripPatterns");
            
            if (patterns.isMissingNode() || patterns.isNull()) {
                log.warn("No tripPatterns found in response");
                return tripPatterns;
            }

            if (!patterns.isArray() || patterns.size() == 0) {
                log.warn("Empty tripPatterns array");
                return tripPatterns;
            }

            log.info("Found {} trip patterns", patterns.size());

            for (JsonNode pattern : patterns) {
                String expectedStartTime = pattern.path("expectedStartTime").asText("");
                String expectedEndTime = pattern.path("expectedEndTime").asText("");
                int duration = pattern.path("duration").asInt(0);
                double walkDistance = pattern.path("walkDistance").asDouble(0);

                List<Leg> legs = new ArrayList<>();
                for (JsonNode legNode : pattern.path("legs")) {
                    String mode = legNode.path("mode").asText("UNKNOWN");
                    double distance = legNode.path("distance").asDouble(0);
                    int legDuration = legNode.path("duration").asInt(0);
                    String legStartTime = legNode.path("expectedStartTime").asText("");
                    String legEndTime = legNode.path("expectedEndTime").asText("");

                    Line line = null;
                    JsonNode lineNode = legNode.path("line");
                    if (!lineNode.isMissingNode() && !lineNode.isNull()) {
                        String lineId = lineNode.path("id").asText("");
                        String publicCode = lineNode.path("publicCode").asText("");
                        String transportMode = lineNode.path("transportMode").asText(mode);
                        line = new Line(lineId, publicCode, transportMode);
                    }

                    Place fromPlace = null;
                    JsonNode fromNode = legNode.path("fromPlace");
                    if (!fromNode.isMissingNode() && !fromNode.isNull()) {
                        fromPlace = new Place(fromNode.path("name").asText("Unknown"));
                    }

                    Place toPlace = null;
                    JsonNode toNode = legNode.path("toPlace");
                    if (!toNode.isMissingNode() && !toNode.isNull()) {
                        toPlace = new Place(toNode.path("name").asText("Unknown"));
                    }

                    legs.add(new Leg(mode, distance, legDuration, legStartTime, legEndTime, 
                                    line, fromPlace, toPlace));
                }

                tripPatterns.add(new TripPattern(expectedStartTime, expectedEndTime, 
                                                 duration, walkDistance, legs));
            }

            log.info("Returning {} trip patterns", tripPatterns.size());

        } catch (Exception e) {
            log.error("Error parsing trip JSON: {}", e.getMessage(), e);
        }

        return tripPatterns;
    }
}