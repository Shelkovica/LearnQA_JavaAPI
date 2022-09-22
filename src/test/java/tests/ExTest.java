package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import org.junit.jupiter.api.Test;
import io.restassured.http.Headers;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExTest {

    @Test
    public void testEx5() {
        JsonPath response = RestAssured
                .given()
                .get ("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        String message = response.get("messages[1].message");
        System.out.println("Second message: "+message);
    }

   /* @Test
    public void testEx6() {
       // Map<String, String > params = new HashMap<>();
      //  Map<String, String> headers = new HashMap<>();
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get ("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

       // response.print();
        System.out.println(response.getCookies());
        System.out.println(response.getHeaders());
        System.out.println(response.getStatusCode());
//response.prettyPrint();

    }*/

    @Test
    public void testEx10() {
    String checkString = "Какая-то строка!";
    assertTrue(checkString.length()>15, "Length checkString no more than 15: Length checkString="+checkString.length());
    }

    @Test
    public void testEx11() {
        Response response = RestAssured
                .given()
                .get ("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        String expectedCookieValue = "hw_value";
        assertEquals(expectedCookieValue, response.getCookie("HomeWork"), "Received cookie with value: "+response.getCookie("HomeWork"));
    }

    @Test
    public void testEx12() {
        Response response = RestAssured
                .given()
                .get ("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        String expectedHeadersValue = "Some secret value";
        assertEquals(expectedHeadersValue, response.getHeader("x-secret-homework-header"), "Received header with value: "+response.getHeader("x-secret-homework-header"));
    }
}