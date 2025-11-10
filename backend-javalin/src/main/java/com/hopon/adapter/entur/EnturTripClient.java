package com.hopon.adapter.entur;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
     */
    public List<TripLeg> planTripByIds(String fromId, String toId) {
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

    private List<TripLeg> parseTrips(String json) {
        List<TripLeg> legs = new ArrayList<>();
        try {
            JsonNode root = mapper.readTree(json);
            
            JsonNode errors = root.path("errors");
            if (errors.isArray() && errors.size() > 0) {
                log.error("Entur API error: {}", errors.get(0).path("message").asText());
                return legs;
            }

            JsonNode patterns = root.path("data").path("trip").path("tripPatterns");
            
            if (patterns.isMissingNode() || patterns.isNull()) {
                log.warn("No tripPatterns found in response");
                return legs;
            }

            if (!patterns.isArray() || patterns.size() == 0) {
                log.warn("Empty tripPatterns array");
                return legs;
            }

            log.info("Found {} trip patterns", patterns.size());

            for (JsonNode pattern : patterns) {
                for (JsonNode leg : pattern.path("legs")) {
                    String mode = leg.path("mode").asText("UNKNOWN");
                    String from = leg.path("fromPlace").path("name").asText("Unknown");
                    String to = leg.path("toPlace").path("name").asText("Unknown");
                    String line = leg.path("line").path("publicCode").asText("");
                    String transportMode = leg.path("line").path("transportMode").asText(mode);
                    double distance = leg.path("distance").asDouble(0);
                    int duration = leg.path("duration").asInt(0);
                    String startTime = leg.path("expectedStartTime").asText("");
                    String endTime = leg.path("expectedEndTime").asText("");

                    legs.add(new TripLeg(from, to, mode, line, transportMode, distance, duration, startTime, endTime));
                }
            }

            log.info("Returning {} trip legs total", legs.size());

        } catch (Exception e) {
            log.error("Error parsing trip JSON: {}", e.getMessage(), e);
        }

        return legs;
    }

    public static class TripLeg {
        public final String from;
        public final String to;
        public final String mode;
        public final String line;
        public final String transportMode;
        public final double distance;
        public final int duration;
        public final String startTime;
        public final String endTime;

        public TripLeg(String from, String to, String mode, String line, String transportMode,
                       double distance, int duration, String startTime, String endTime) {
            this.from = from;
            this.to = to;
            this.mode = mode;
            this.line = line;
            this.transportMode = transportMode;
            this.distance = distance;
            this.duration = duration;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public String getFrom() { return from; }
        public String getTo() { return to; }
        public String getMode() { return mode; }
        public String getLine() { return line; }
        public String getTransportMode() { return transportMode; }
        public double getDistance() { return distance; }
        public int getDuration() { return duration; }
        public String getStartTime() { return startTime; }
        public String getEndTime() { return endTime; }

        @Override
        public String toString() {
            return String.format("%s -> %s [%s %s] %.0fm (%ds)", 
                from, to, mode, line, distance, duration);
        }
    }
}