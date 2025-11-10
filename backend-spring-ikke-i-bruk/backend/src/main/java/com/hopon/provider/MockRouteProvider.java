package com.hopon.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopon.domain.*;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class MockRouteProvider implements RouteProvider {

    private final List<Itinerary> cached;

    public MockRouteProvider() {
        this.cached = loadFromJson();
    }

    @Override
    public List<Itinerary> find(TripRequest request) {
        return cached; // v0: alltid samme resultat
    }

    private List<Itinerary> loadFromJson() {
        try (InputStream is = getClass().getResourceAsStream("/mock/routes.json")) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(is);

            List<Itinerary> result = new ArrayList<>();
            for (JsonNode it : root.get("itineraries")) {
                int total = it.get("totalMinutes").asInt();
                List<Leg> legs = new ArrayList<>();
                for (JsonNode leg : it.get("legs")) {
                    Place from = new Place(
                            leg.get("from").get("id").asText(),
                            leg.get("from").get("name").asText()
                    );
                    Place to = new Place(
                            leg.get("to").get("id").asText(),
                            leg.get("to").get("name").asText()
                    );
                    Mode mode = Mode.valueOf(leg.get("mode").asText());
                    int minutes = leg.get("durationMinutes").asInt();

                    legs.add(new Leg(from, to, mode, minutes));
                }
                result.add(new Itinerary(legs, total));
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load routes.json", e);
        }
    }
}
