package com.hopon.app;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hopon.adapter.database.MySqlFavoriteTripRepository;
import com.hopon.adapter.entur.EnturClient;
import com.hopon.adapter.entur.EnturLocationClient;
import com.hopon.adapter.entur.EnturTripClient;
import com.hopon.core.model.FavoriteTrip;
import com.hopon.core.service.DepartureService;

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        // ---- JSON konfig ----
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        JavalinJackson jackson = new JavalinJackson(mapper);

        // ---- Start server ----
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(jackson);
            config.plugins.enableCors(cors -> {
                cors.add(it -> {
                    it.anyHost();
                });
            });
        }).start(8080);

        // ---- Opprett tjenester ----
        var enturClient = new EnturClient();
        var departureService = new DepartureService(enturClient);
        var locationClient = new EnturLocationClient();
        var tripClient = new EnturTripClient();
        
        // Database konfigurasjon
        String dbUrl = "jdbc:mysql://localhost:3306/hopon?useSSL=false&serverTimezone=UTC";
        String dbUser = "root";
        String dbPassword = "Bacman124A";
        var favoriteRepo = new MySqlFavoriteTripRepository(dbUrl, dbUser, dbPassword);

        // ---- Health check ----
        app.get("/health", ctx -> ctx.result("OK"));

        // ---- Entur geocoder proxy ----
        app.get("/search", ctx -> {
            if (!RequestValidator.requireQueryParam(ctx, "name")) return;
            var name = ctx.queryParam("name");
            var results = locationClient.findStopId(name);
            ctx.json(results);
        });

        // ---- Departures ----
        app.get("/departures", ctx -> {
            if (!RequestValidator.validateNameOrId(ctx)) return;

            var stopInput = RequestValidator.getStopInput(ctx);
            var result = departureService.getDepartures(stopInput);

            if (result == null || result.isEmpty()) {
                ctx.status(404).result("Ingen avganger funnet for " + stopInput);
            } else {
                ctx.json(result);
            }
        });

        // ---- Reiseplanlegger (trip) ----
        app.get("/trip", ctx -> {
            if (!RequestValidator.requireFromAndTo(ctx)) return;

            String from = ctx.queryParam("from");
            String to = ctx.queryParam("to");

            // Prøv å finne StopPlace IDs direkte
            String fromId = locationClient.findFirstStopPlaceId(from);
            String toId = locationClient.findFirstStopPlaceId(to);

            // Hvis ikke funnet, hent koordinater og finn nærmeste holdeplass
            if (fromId == null) {
                var fromGeo = locationClient.findFirstGeoResult(from);
                if (fromGeo != null) {
                    try {
                        fromId = locationClient.findNearestStopPlace(fromGeo.lat(), fromGeo.lon());
                    } catch (Exception e) {
                        log.error("Error finding nearest stop for FROM: {}", e.getMessage());
                    }
                }
            }

            if (toId == null) {
                var toGeo = locationClient.findFirstGeoResult(to);
                if (toGeo != null) {
                    try {
                        toId = locationClient.findNearestStopPlace(toGeo.lat(), toGeo.lon());
                    } catch (Exception e) {
                        log.error("Error finding nearest stop for TO: {}", e.getMessage());
                    }
                }
            }

            // Hvis fortsatt ikke funnet, gi feilmelding
            if (fromId == null || toId == null) {
                String msg = "Kunne ikke finne holdeplasser";
                if (fromId == null) msg += " for '" + from + "'";
                if (toId == null) msg += (fromId == null ? " eller" : "") + " for '" + to + "'";
                ctx.status(404).result(msg + ". Prøv med holdeplassnavn i stedet for gateadresse.");
                return;
            }

            var tripPatterns = tripClient.planTripByIds(fromId, toId);

            if (tripPatterns.isEmpty()) {
                ctx.status(404).result("Ingen reiser funnet fra " + from + " til " + to);
            } else {
                ctx.json(Map.of(
                    "data", Map.of(
                        "trip", Map.of(
                            "tripPatterns", tripPatterns
                        )
                    )
                ));
            }
        });

        // ---- Favoritter ----
        // Hent alle favoritter
        app.get("/favorites", ctx -> {
            var favorites = favoriteRepo.findAll();
            ctx.json(Map.of("value", favorites, "Count", favorites.size()));
        });

        // Lagre ny favoritt
        app.post("/favorites", ctx -> {

            String from = ctx.queryParam("from");
            String to = ctx.queryParam("to");

            if (from == null || to == null) {
                ctx.status(400).result("----Query params 'from' and 'to' are required----");
                return;
            }
            
            FavoriteTrip trip = new FavoriteTrip(from, to);
            FavoriteTrip saved = favoriteRepo.save(trip);
            
            if (saved != null) {
                ctx.status(201).json(saved);
            } else {
                ctx.status(500).result("Failed to save favorite");
            }
        });

        // Slett favoritt
        app.delete("/favorites/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean deleted = favoriteRepo.delete(id);
            
            if (deleted) {
                ctx.status(204);
            } else {
                ctx.status(404).result("Favorite not found");
            }
        });
    } 
}
