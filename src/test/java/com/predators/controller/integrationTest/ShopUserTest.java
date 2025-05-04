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
public class ShopUserTest {

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
                .get("v1/users")
                .then()
                .statusCode(200);
    }

    @Test
    void testCreate() {
        given()
                .port(port)
                .contentType("application/json")
                .body("{\"name\":\"real test\", " +
                        "\"email\":\"realTest\", " +
                        "\"phone\":\"123456789\", " +
                        "\"password\":\"12345\"}")
                .when()
                .post("v1/users/register")
                .then()
                .statusCode(201);
    }

    @Test
    void testNotCreatWithExistingEmail() {
        given()
                .port(port)
                .contentType("application/json")
                .body("{\"name\":\"real test\", " +
                        "\"email\":\"test\", " +
                        "\"phone\":\"123456789\", " +
                        "\"password\":\"12345\"}")
                .when()
                .post("v1/users/register")
                .then()
                .statusCode(409);
    }

    @Test
    void testNotCreatWithEmptyEmail() {
        given()
                .port(port)
                .contentType("application/json")
                .body("{\"name\":\"nane\", " +
                        "\"email\":\"\", " +
                        "\"phone\":\"123456789\", " +
                        "\"password\":\"12345\"}")
                .when()
                .post("v1/users/register")
                .then()
                .statusCode(400);
    }

    @Test
    void testNotCreatWithEmptyPassword() {
        given()
                .port(port)
                .contentType("application/json")
                .body("{\"name\":\"something\", " +
                        "\"email\":\"something\", " +
                        "\"phone\":\"123456789\", " +
                        "\"password\":\"\"}")
                .when()
                .post("v1/users/register")
                .then()
                .statusCode(400);
    }

    @Test
    void testNotCreatWithEmptyNumber() {
        given()
                .port(port)
                .contentType("application/json")
                .body("{\"name\":\"something\", " +
                        "\"email\":\"something\", " +
                        "\"phone\":\"\", " +
                        "\"password\":\"12345\"}")
                .when()
                .post("v1/users/register")
                .then()
                .statusCode(400);
    }

    @Test
    void testNotCreatWithEmptyName() {
        given()
                .port(port)
                .contentType("application/json")
                .body("{\"name\":\"\", " +
                        "\"email\":\"something\", " +
                        "\"phone\":\"123456789\", " +
                        "\"password\":\"12345\"}")
                .when()
                .post("v1/users/register")
                .then()
                .statusCode(400);
    }

    @Test
    void testLogin() {
        given()
                .port(port)
                .contentType("application/json")
                .body("{\"email\":\"test\", " +
                        "\"password\":\"12345\"}")
                .when()
                .post("v1/users/login")
                .then()
                .statusCode(200);
    }

    @Test
    void testNotLoginWithEmptyEmail() {
        given()
                .port(port)
                .contentType("application/json")
                .body("{\"email\":\"\", " +
                        "\"password\":\"12345\"}")
                .when()
                .post("v1/users/login")
                .then()
                .statusCode(400);
    }

    @Test
    void testNotLoginWithNotExistingUser() {
        given()
                .port(port)
                .contentType("application/json")
                .body("{\"email\":\"test5000\", " +
                        "\"password\":\"12345\"}")
                .when()
                .post("v1/users/login")
                .then()
                .statusCode(404);
    }

    @Test
    void testNotLoginWithEmptyPassword() {
        given()
                .port(port)
                .contentType("application/json")
                .body("{\"email\":\"test\", " +
                        "\"password\":\"\"}")
                .when()
                .post("v1/users/login")
                .then()
                .statusCode(401);
    }

    @Test
    void testGetById() {
        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("v1/users/1")
                .then()
                .statusCode(200);
    }

    @Test
    void testGetByIdNotFound() {
        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("v1/users/111")
                .then()
                .statusCode(404);
    }

    @Test
    void testDelete() {
        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("v1/users/1")
                .then()
                .statusCode(200);
    }

    @Test
    void testNotDeleteWithFalseUserId() {
        given()
                .port(port)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("v1/users/111")
                .then()
                .statusCode(404);
    }
}
