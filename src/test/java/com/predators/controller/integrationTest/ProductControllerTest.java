package com.predators.controller.integrationTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
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
class ProductControllerTest {

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
    void testGetAll() {
        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("v1/products")
                .then()
                .statusCode(200);
    }

    @Test
    void testGetById() {
        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("v1/products/1")
                .then()
                .statusCode(200);
    }

    @Test
    void testCreate() {
        String product = """
                {
                  "name": "one",
                  "description": "one description",
                  "price": "20.89",
                  "categoryId": "1",
                  "image": "http/"
                }""";
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(product)
                .header("Authorization", "Bearer " + token)
                .when()
                .post("v1/products")
                .then()
                .statusCode(201);
    }

    @Test
    void testUpdate() {
        String product = """
                {
                  "name": "one",
                  "description": "one description",
                  "price": "10.12",
                  "categoryId": "1",
                  "image": "http/"
                }""";
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(product)
                .header("Authorization", "Bearer " + token)
                .when()
                .put("v1/products/1")
                .then()
                .statusCode(201);
    }


    @Test
    @Transactional
    void testDelete() {
        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("v1/products/2")
                .then()
                .statusCode(200);
    }

}