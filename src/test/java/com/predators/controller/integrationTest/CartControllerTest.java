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
class CartControllerTest {

    @LocalServerPort
    private int port;

    private String token;

    @BeforeEach
    public void init() {
        RestAssured.baseURI = "http://localhost:" + port;
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
    void getAllCartItems() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("v1/cart")
                .then()
                .statusCode(200);
    }

    @Test
    void addProduct() {
        given()
                .contentType("application/json")
                .body("{\n" +
                        "    \"productId\": 1,\n" +
                        "    \"quantity\": 15\n" +
                        "}")
                .header("Authorization", "Bearer " + token)
                .when()
                .post("v1/cart")
                .then()
                .statusCode(200);
    }

    @Test
    void deleteProduct() {
        given()
                .header("Authorization", "Bearer " + token)
                .port(port)
                .when()
                .delete("v1/cart/1")
                .then()
                .statusCode(200);
    }
}