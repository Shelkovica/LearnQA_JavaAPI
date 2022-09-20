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
        Map<String, String > params = new HashMap<>();
        JsonPath response = RestAssured
                .given()
                .get ("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        String message = response.get("messages[1].message");
        System.out.println("Second message: "+message);
    }
}
