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
public class FavoriteControllerIntegrationTest {

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
    void testCreateFavorite() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .post("v1/favorites/7")
                .then()
                .statusCode(201);
    }

    @Test
    void testNotCreateExistingFavorite() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .post("v1/favorites/3")
                .then()
                .statusCode(409);
    }

    @Test
    void testGetAllFavorites() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("v1/favorites")
                .then()
                .statusCode(200);
    }

    @Test
    void testGetFavoriteById() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("v1/favorites/1")
                .then()
                .statusCode(200);
    }

    @Test
    void testDeleteFavorite() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("v1/favorites/1")
                .then()
                .statusCode(200);
    }
}