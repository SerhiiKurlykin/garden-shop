package com.predators.controller.integrationTest;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext
class OrderControllerTest {

    @LocalServerPort
    private int port;

    private String token;
    @BeforeEach
    public void init() {
        RestAssured.baseURI = "http://localhost";
        token = given()
                .contentType("application/json")
                .body("{\"email\":\"test\", \"password\":\"12345\"}")
                .when()
                .post("v1/users/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    @Test
    void getAll() {
        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/v1/orders")
                .then()
                .statusCode(200);
    }

    @Test
    void getStatus() {
        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/v1/orders/get-status/CREATED")
                .then()
                .statusCode(200);
    }

    @Test
    void create() {
        given()
                .port(port)
                .contentType("application/json")
                .body("{\n" +
                        "  \"items\": [\n" +
                        "    {\n" +
                        "      \"productId\": 1,\n" +
                        "      \"quantity\": 11\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"productId\": 2,\n" +
                        "      \"quantity\": 12\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"deliveryAddress\": \"xz\",\n" +
                        "  \"deliveryMethod\": \"SELF_PICKUP\"\n" +
                        "}")
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/v1/orders")
                .then()
                .statusCode(201);
    }

    @Test
    void getById() {
        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/v1/orders/1")
                .then()
                .statusCode(200);
    }

    @Test
    void updateStatus() {
        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .when()
                .put("/v1/orders/1/status?status=CREATED")
                .then()
                .statusCode(200);
    }

    @Test
    void delete() {
        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/v1/orders/2")
                .then()
                .statusCode(200);
    }
}