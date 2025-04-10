package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class Main extends AbstractVerticle {
    @Override
    public void start() {
        vertx.createHttpServer()
            .requestHandler(req -> req.response().end("Hello from Vert.x!"))
            .listen(8888);
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new Main());
    }
}
