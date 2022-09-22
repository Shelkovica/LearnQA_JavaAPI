package tests;//package test.java;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HelloWorldTest {
    @ParameterizedTest
    @ValueSource(strings = {"", "John", "Pete"})
    public void testHelloMethodWithoutName(String name) {
        Map<String, String> queryParams = new HashMap<>();
        if(name.length()>0){
            queryParams.put("name", name);
        }

        JsonPath response = RestAssured
                .given()
                .queryParams(queryParams)
                .get ("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String answer = response.getString("answer");
        String expectedName = (name.length() > 0 ) ? name: "someone";  //тернанрый оператор
        assertEquals("Hello, "+expectedName, answer, "The answer is not expected");
    }


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
                .when()
                .get ("https://playground.learnqa.ru/api/get_303")
                .andReturn();
        //int statusCode = response.getStatusCode();
        response.prettyPrint();

        String locationHeaders = response.getHeader("Location");
        System.out.println(locationHeaders);
    }

    @Test
    public void testRestAssured3() {
        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login1");
        data.put("password", "secret_pass1");
        Response responseForGet = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String responseCookie = responseForGet.getCookie("auth_cookie");
      /*  System.out.println("\nPretty text:");
        responseForGet.prettyPrint();

        System.out.println("\nHeaders:");
        Headers responseHeaders = responseForGet.getHeaders();
        System.out.println(responseHeaders);

        System.out.println("\nCookies:");
        Map<String, String> responseCookies  = responseForGet.getCookies();
        System.out.println(responseCookies);

        String responseCookie1 = responseForGet.getCookie("auth_cookie");
        System.out.println(responseCookie1);*/

        Map<String, String> cookies  = new HashMap<>();
        if(responseCookie != null) {
            cookies.put("auth_cookie", responseCookie);
        }
        Response responseForCheck = RestAssured
                .given()
                .body(data)
                .cookies(cookies)
                .when()
                .post("https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();

        responseForCheck.print();

    }

    @Test
    public void testFor200() {
        Response response = RestAssured
                .get ("https://playground.learnqa.ru/api/map")
                .andReturn();
       // assertTrue(response.statusCode() == 200, "Unexpected status code");
        assertEquals(200, response.statusCode(), "Unexpected status code");
    }

    @Test
    public void testFor404() {
        Response response = RestAssured
                .get ("https://playground.learnqa.ru/api/map1")
                .andReturn();
        // assertTrue(response.statusCode() == 200, "Unexpected status code");
        assertEquals(404, response.statusCode(), "Unexpected status code");
    }




}

