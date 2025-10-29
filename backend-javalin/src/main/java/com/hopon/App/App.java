package com.hopon.App;

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.hopon.Core.service.DepartureService;
import com.hopon.Core.service.FavoriteService;
import com.hopon.Core.ports.FavoriteRepository;
import com.hopon.Core.repository.InMemoryFavoriteRepository;
import com.hopon.adapter.entur.EnturClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    //  Definer logger øverst i klassen
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        log.info("🚀 Starting HopOn backend application...");

        //  Jackson-oppsett for LocalDateTime
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        JavalinJackson jackson = new JavalinJackson(mapper);

        //  Start Javalin-server med custom mapper
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(jackson);
        }).start(8080);

        log.info("✅ HopOn backend running on http://localhost:8080");

        //  Opprett tjenester og repositories
        var entur = new EnturClient();
        var departureService = new DepartureService(entur);
        FavoriteRepository favRepo = new InMemoryFavoriteRepository();
        FavoriteService favoriteService = new FavoriteService(favRepo);

        //  Definer API-endepunkter med logging
        app.get("/departures", ctx -> {
            String stopId = ctx.queryParam("id");
            log.info("GET /departures?id={}", stopId);
            ctx.json(departureService.getDepartures(stopId));
        });

        app.get("/favorites", ctx -> {
            log.info("GET /favorites");
            ctx.json(favoriteService.getAll());
        });

        app.post("/favorites", ctx -> {
            String stopId = ctx.body();
            log.info("POST /favorites body={}", stopId);
            favoriteService.add(stopId);
            ctx.status(201);
        });

        app.delete("/favorites/{id}", ctx -> {
            String id = ctx.pathParam("id");
            log.info("DELETE /favorites/{}", id);
            favoriteService.remove(id);
            ctx.status(204);
        });

        //  Logg når applikasjonen avsluttes
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
            log.info("🛑 HopOn backend shutting down...")
        ));
    }
}
