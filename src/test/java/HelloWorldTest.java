package test.java;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {
    @Test
    public void testRestAssured() {
        Map<String, String > params = new HashMap<>();
        params.put("name", "John");
        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .get ("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String name = response.get("answer");
        if (name == null){
            System.out.println("The key 'answer2' is absent");
        } else {
            System.out.println(name);
        }


    }

    @Test
    public void testRestAssured1() {
        Map<String, Object > body = new HashMap<>();
        body.put("param1","value1");
        body.put("param2","value2");
        Response response = RestAssured
                .given()
                .body(body)
                .post ("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        response.print();


    }

    @Test
    public void testRestAssured2() {
        Map<String, String> headers = new HashMap<>();
        headers.put("myHeader1", "myValue1");
        headers.put("myHeader2", "myValue2");
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
               // .headers(headers)
                .when()
                .get ("https://playground.learnqa.ru/api/get_303")
                .andReturn();
        //int statusCode = response.getStatusCode();
        response.prettyPrint();

        String locationHeaders = response.getHeader("Location");
        System.out.println(locationHeaders);



    }
}

