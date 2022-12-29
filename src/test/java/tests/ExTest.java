package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.restassured.http.Headers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.lang.Thread;
import java.util.HashMap;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExTest extends BaseTestCase {

    private  final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("This test fo")
    @DisplayName("Test positive auth user")
    public void testEx5() {
        JsonPath response = RestAssured
                .given()
                .get ("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        String message = response.get("messages[1].message");
        System.out.println("Second message: "+message);
    }

    @Test
    public void testEx6() {

        Map<String, String > params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get ("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        System.out.println(response.getHeader("Location"));
        System.out.println(response.getStatusCode());

    }

    @Test
    public void testEx7() {

        Map<String, String > params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        int count_redirect = 0;
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get ("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
        String new_url = response.getHeader("Location");
        while(new_url !=null) {
            System.out.println(new_url);
            count_redirect = count_redirect+1 ;
            Response responseNew = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get (new_url)
                    .andReturn();
            new_url = responseNew.getHeader("Location");

        }
        System.out.println(count_redirect);

    }

    @Test
    public void testEx8() throws InterruptedException {

        Response response = apiCoreRequests.makeGetRequestWithoutCookieAndToken("https://playground.learnqa.ru/ajax/api/longtime_job");
        String token = response.jsonPath().get("token");
        Integer seconds = response.jsonPath().get("seconds");

        Response responseBeforeTask = apiCoreRequests.makeGetRequestWithoutCookieAndToken("https://playground.learnqa.ru/ajax/api/longtime_job?token="+token );
        Assertions.assertJsonByName(responseBeforeTask, "status","Job is NOT ready" );
        Thread.sleep((seconds+1)*1000);

        Response responseAfterTask = apiCoreRequests.makeGetRequestWithoutCookieAndToken("https://playground.learnqa.ru/ajax/api/longtime_job?token="+token );

        Assertions.assertJsonByName(responseAfterTask, "status","Job is ready" );
        Assertions.assertJsonHasField(responseAfterTask, "result");
    }

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

    @ParameterizedTest
    /*@ValueSource(strings = {"Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
            "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
            "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1"
    })*/
    @CsvSource(value = {"'Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30', Mobile, No, Android",
            "'Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1', Mobile, Chrome, iOS",
            "'Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)', Googlebot, Unknown, Unknown",
            "'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0', Web, Chrome, No",
            "'Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1', Mobile, No, iPhone"
    })
    public void testEx13(String param_agent, String platform_expected, String browser_expected, String  device_expected) {

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("param_agent", param_agent);
        queryParams.put("platform_expected", platform_expected);
        queryParams.put("browser_expected", browser_expected);
        queryParams.put("device_expected", device_expected);

        JsonPath response = RestAssured
                .given()
                .header("User-Agent",param_agent )
                .get ("https://playground.learnqa.ru/api/user_agent_check")
                .jsonPath();
        response.prettyPrint();

        String platform = response.getJsonObject("platform");
        String browser = response.getJsonObject("browser");
        String device = response.getJsonObject("device");

        System.out.println(param_agent);
        if(!platform.equals(platform_expected)){
            System.out.println("platform is wrong: "+platform +"; expected: "+platform_expected);
        }
        if(!browser.equals(browser_expected)){
                    System.out.println("browser is wrong: "+browser +"; expected: "+browser_expected);
        }
        if(!device.equals(device_expected)){
            System.out.println("device is wrong: "+platform +"; expected: "+device_expected);
        }

        assertTrue((platform.equals(platform_expected))&(browser.equals(browser_expected))&(device.equals(device_expected)) , "User Agent id wrong" );
       }



}