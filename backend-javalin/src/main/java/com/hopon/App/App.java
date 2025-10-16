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

public class App {
    public static void main(String[] args) {
        //  Jackson setup for LocalDateTime
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        JavalinJackson jackson = new JavalinJackson(mapper);

        //  Start server med custom mapper
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(jackson);
        }).start(8080);

        System.out.println("🚀 HopOn backend running on http://localhost:8080");

        // Services
        var entur = new EnturClient();
        var departureService = new DepartureService(entur);
        FavoriteRepository favRepo = new InMemoryFavoriteRepository();
        FavoriteService favoriteService = new FavoriteService(favRepo);

        // Routes
        app.get("/departures", ctx -> {
            String stopId = ctx.queryParam("id");
            ctx.json(departureService.getDepartures(stopId));
        });

        app.get("/favorites", ctx -> ctx.json(favoriteService.getAll()));

        app.post("/favorites", ctx -> {
            String stopId = ctx.body();
            favoriteService.add(stopId);
            ctx.status(201);
        });

        app.delete("/favorites/{id}", ctx -> {
            favoriteService.remove(ctx.pathParam("id"));
            ctx.status(204);
        });
    }
}
