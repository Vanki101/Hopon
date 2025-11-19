package com.hopon.App;

import io.javalin.Javalin;
import com.hopon.core.service.DepartureService;
import com.hopon.adapter.entur.MockEnturClient;

public class TestApp {
    public static Javalin startTestServer() {
        var mockClient = new MockEnturClient();
        var service = new DepartureService(mockClient);

        Javalin app = Javalin.create().start(7070); // egen port for test

        app.get("/departures", ctx -> {
            String id = ctx.queryParam("id");
            ctx.json(service.getDepartures(id));
        });

        return app;
    }
}
