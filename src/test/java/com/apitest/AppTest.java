package com.apitest;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class AppTest
{
    @Test
    void shouldFindJobPostById(Object[] params){
        String url = (String) params[0];
        String token = (String) params[1];
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(url)
                .then()
                .statusCode(200);
    }

    @Test
    void testRandomDogBreed(){
        given().contentType(ContentType.JSON)
                .when()
                .get("https://dog.ceo/api/breeds/image/random/3")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"));
    }

     @Test
    public void testGetRandomCatBreeds(Object[] params) {
        String url = (String) params[0];
        given()
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .body("total", equalTo(98));
    }
}
