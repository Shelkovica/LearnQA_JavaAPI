import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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
}
