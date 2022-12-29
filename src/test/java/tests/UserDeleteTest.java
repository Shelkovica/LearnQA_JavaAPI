package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Features;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserDeleteTest extends BaseTestCase {
    String cookie;
    String header;
    private  final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    @Features(value={@Feature(value="Login user"), @Feature(value="Delete user")})
    @Description("This test failed deleted system user after auth")
    @DisplayName("Test deleted system user")
    public void testDeletedSystemUser(){
        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        // DELETE
        String newMail = DataGenerator.getRandomEmailWithoutAt();
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newMail);

        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/2", this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    @Features(value={@Feature(value="Login user"), @Feature(value="Create user"), @Feature(value="Delete user"), @Feature(value="Get user")})
    @Description("This test success deleted just user after created")
    @DisplayName("Test deleted just user")
    public void testDeletedJustCreatedUser(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestWithJsonResponse("https://playground.learnqa.ru/api/user/", userData);

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        // DELETE
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/"+userId, this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertResponseCodeEquals(responseDeleteUser, 200);

        //GET
        Response responseGetUser = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/"+userId, this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));
        Assertions.assertResponseTextEquals(responseGetUser, "User not found");
        Assertions.assertResponseCodeEquals(responseGetUser, 404);
    }

    @Test
    @Features(value={@Feature(value="Login user"), @Feature(value="Create user"), @Feature(value="Delete user"), @Feature(value="Get user")})
    @Description("This test failed deleted just user after created")
    @DisplayName("Test failed deleted just user")
    public void testDeletedJustCreatedUserBySecondUser(){
        //GENERATE FIRST USER
        Map<String, String> userData = DataGenerator.getRegistrationRandomData();
        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestWithJsonResponse("https://playground.learnqa.ru/api/user/", userData);

        String userId = responseCreateAuth.getString("id");

        //GENERATE SECOND USER
        Map<String, String> userDataTwo = DataGenerator.getRegistrationRandomData();
        JsonPath responseCreateAuthTwo = apiCoreRequests
                .makePostRequestWithJsonResponse("https://playground.learnqa.ru/api/user/", userDataTwo);

        String userIdTwo = responseCreateAuthTwo.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        // DELETE
        System.out.println(authData);
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/"+userIdTwo, this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertResponseCodeEquals(responseDeleteUser, 200);

        //GET
        Response responseGetUser = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/"+userIdTwo, this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertResponseTextEquals(responseGetUser, "User not found");
        Assertions.assertResponseCodeEquals(responseGetUser, 404);
    }

}