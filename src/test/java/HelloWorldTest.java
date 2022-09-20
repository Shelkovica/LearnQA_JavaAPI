//package test.java;
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
}

