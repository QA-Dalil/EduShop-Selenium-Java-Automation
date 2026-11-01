package com.qadalil.edushop.support;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.json.Json;

public final class EduShopApi {
  private static final String API_URL = System.getProperty("apiUrl", "http://localhost:5000").replaceAll("/+$", "");
  private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
  private final Json json = new Json();

  public String login(String email, String password) {
    Map<String, Object> response = request("POST", "/api/auth/login", null,
        Map.of("email", email, "password", password));
    return (String) response.get("accessToken");
  }

  public Map<String, Object> order(int id, String customerToken) {
    return request("GET", "/api/order/" + id, customerToken, null);
  }

  public Map<String, Object> currentUser(String customerToken) {
    return request("GET", "/api/auth/me", customerToken, null);
  }

  public void ensureLaptopStock(int minimum, int replenishedStock) {
    Map<String, Object> listing = request("GET", "/api/product?searchTerm=Laptop", null, null);
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> items = (List<Map<String, Object>>) listing.get("items");
    Map<String, Object> laptop = items.stream()
        .filter(item -> ((String) item.get("name")).startsWith("Laptop Pro 15"))
        .findFirst().orElseThrow(() -> new IllegalStateException("Seeded Laptop Pro 15 product is missing"));
    int stock = ((Number) laptop.get("stock")).intValue();
    if (stock >= minimum) return;

    String adminEmail = System.getenv("EDUSHOP_ADMIN_EMAIL");
    String adminPassword = System.getenv("EDUSHOP_ADMIN_PASSWORD");
    if (adminEmail == null || adminPassword == null || adminEmail.isBlank() || adminPassword.isBlank()) {
      throw new IllegalStateException("Laptop stock is " + stock
          + "; set EDUSHOP_ADMIN_EMAIL and EDUSHOP_ADMIN_PASSWORD to replenish it before the purchase test");
    }
    String token = login(adminEmail, adminPassword);
    Map<String, Object> update = Map.of(
        "name", laptop.get("name"),
        "description", laptop.get("description"),
        "price", laptop.get("price"),
        "stock", replenishedStock,
        "imageUrl", laptop.get("imageUrl"),
        "categoryId", laptop.get("categoryId"),
        "discountPercentage", laptop.get("discountPercentage"),
        "isActive", laptop.get("isActive"));
    Map<String, Object> updated = request("PUT", "/api/product/" + laptop.get("id"), token, update);
    if (((Number) updated.get("stock")).intValue() != replenishedStock) {
      throw new IllegalStateException("Admin stock update did not return the requested quantity");
    }
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> request(String method, String path, String token, Object body) {
    HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(API_URL + path))
        .timeout(Duration.ofSeconds(20)).header("Accept", "application/json");
    if (token != null) builder.header("Authorization", "Bearer " + token);
    if (body == null) builder.method(method, HttpRequest.BodyPublishers.noBody());
    else builder.header("Content-Type", "application/json")
        .method(method, HttpRequest.BodyPublishers.ofString(json.toJson(body)));
    try {
      HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() < 200 || response.statusCode() >= 300) {
        throw new IllegalStateException(method + " " + path + " returned HTTP " + response.statusCode()
            + ": " + response.body());
      }
      return json.toType(response.body(), Map.class);
    } catch (IOException exception) {
      throw new IllegalStateException("API request failed: " + method + " " + path, exception);
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("API request interrupted: " + method + " " + path, exception);
    }
  }
}
