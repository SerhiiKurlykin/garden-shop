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
    void testGetAll() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("v1/users")
                .then()
                .statusCode(200);
    }

    @Test
    void testCreate() {
        given()
                .contentType("application/json")
                .body("{\"name\":\"realtest\", " +
                        "\"email\":\"someeamil@gamil.com\", " +
                        "\"phone\":\"+1234567895\", " +
                        "\"password\":\"12345\"}")
                .when()
                .post("v1/users/register")
                .then()
                .statusCode(201);
    }

    @Test
    void testNotCreateWithExistingEmail() {
        given()
                .contentType("application/json")
                .body("{\"name\":\"realtest\", " +
                        "\"email\":\"someemail@gamil.com\", " +
                        "\"phone\":\"+1234567895\", " +
                        "\"password\":\"12345\"}")
                .when()
                .post("v1/users/register")
                .then()
                .statusCode(409);
    }

    @Test
    void testNotCreateWithEmptyEmail() {
        given()
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
    void testNotCreateWithEmptyPassword() {
        given()
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
    void testNotCreateWithEmptyNumber() {
        given()
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
    void testNotCreateWithEmptyName() {
        given()
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
                .contentType("application/json")
                .body("{\"email\":\"test\", " +
                        "\"password\":\"12345\"}")
                .when()
                .post("v1/users/login")
                .then()
                .statusCode(200);
    }

//    @Test
//    void testNotLoginWithEmptyEmail() {
//        given()
//                .contentType("application/json")
//                .body("{\"email\":\" \", " +
//                        "\"password\":\"12345\"}")
//                .when()
//                .post("v1/users/login")
//                .then()
//                .statusCode(400);
//        // как валидировать авторизацию???????
//    }

    @Test
    void testNotLoginWithNotExistingUser() {
        given()
                .contentType("application/json")
                .body("{\"email\":\"test5000@gmail.com\", " +
                        "\"password\":\"12345\"}")
                .when()
                .post("v1/users/login")
                .then()
                .statusCode(404);
    }

    @Test
    void testNotLoginWithEmptyPassword() {
        given()
                .contentType("application/json")
                .body("{\"email\":\"someemail@gamil.com\", " +
                        "\"password\":\"\"}")
                .when()
                .post("v1/users/login")
                .then()
                .statusCode(401);
    }

    @Test
    void testGetById() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("v1/users/1")
                .then()
                .statusCode(200);
    }

    @Test
    void testGetByIdNotFound() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("v1/users/111")
                .then()
                .statusCode(404);
    }

    @Test
    void testDelete() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("v1/users/2")
                .then()
                .statusCode(200);
    }

    @Test
    void testNotDeleteWithFalseUserId() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("v1/users/111")
                .then()
                .statusCode(404);
    }
}
