package com.hopon;

import io.javalin.Javalin;

public class HopOnJavalinApplication {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7070);

        app.get("/", ctx -> ctx.result("HopOn Javalin backend is running 🚀"));
    }
}
