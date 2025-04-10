package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.core.json.JsonObject;

import java.util.*;

public class RestfulAPi extends AbstractVerticle {

  private Map<Integer, JsonObject> productStore = new HashMap<>();
  private int nextId = 1;

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new RestfulAPi());
  }

  @Override
  public void start() {
    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());
    router.get("/").handler(wtf -> {
      wtf.response().end("Heello world Vert.x!");
    });
    router.get("/api/products").handler(this::getAllProducts);
    router.get("/api/products/:id").handler(this::getProductById);
    router.post("/api/products").handler(this::addProduct);

    vertx.createHttpServer()
         .requestHandler(router)
         .listen(3000, http -> {
           if (http.succeeded()) {
             System.out.println("RESTAPI is running in http://localhost:3000/api/products");
           }
         });
  }

  private void getAllProducts(RoutingContext ctx) {
    ctx.response()
       .putHeader("Content-Type", "application/json")
       .end(productStore.values().toString());
  }

  private void getProductById(RoutingContext ctx) {
    int id = Integer.parseInt(ctx.pathParam("id"));
    JsonObject product = productStore.get(id);

    if (product != null) {
      ctx.response().putHeader("Content-Type", "application/json")
         .end(product.encodePrettily());
    } else {
      ctx.response().setStatusCode(404)
         .end("Not Found any thing with id " + id);
    }
  }

  private void addProduct(RoutingContext ctx) {
    JsonObject body = ctx.getBodyAsJson();
    if (body == null || body.getString("name") == null) {
      ctx.response().setStatusCode(400).end("There is not data about 'name'");
      return;
    }

    int id = nextId++;
    JsonObject product = new JsonObject()
      .put("id", id)
      .put("name", body.getString("name"));

    productStore.put(id, product);

    ctx.response()
       .setStatusCode(201)
       .putHeader("Content-Type", "application/json")
       .end(product.encodePrettily());
  }
}
