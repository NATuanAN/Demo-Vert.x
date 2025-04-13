package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class MainVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  @Override
  public void start() {
    Router router = Router.router(vertx);
    router.get("/").handler(ctx -> {
      ctx.response().end("Hello world from Vert.x in home page");
    });

    router.get("/api/time").handler(ctx -> {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      String now = LocalDateTime.now().format(formatter);
      ctx.response()
         .putHeader("content-type", "application/json")
         .end("{The now time is: " + now + "}");
    });

    vertx.createHttpServer()
         .requestHandler(router)
         .listen(8888, http -> {
           if (http.succeeded()) {
             System.out.println("Server is running in  http://localhost:8888/");
           } else {
             System.out.println("Some thing went wrong with serve");
           }
         });
  }
}
