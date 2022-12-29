package tests;


import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Features;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {

    private  final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Features(value={@Feature(value="Login user"), @Feature(value="Create user"), @Feature(value="Edit user")})
    @Description("This test successfully edit user after auth")
    @DisplayName("Test positive edit user")
    public void testEditJustCreatedTest(){
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

        // EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequestWithCookieAndToken("https://playground.learnqa.ru/api/user/" + userId, editData, this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertResponseCodeEquals(responseEditUser, 200);
        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId, this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    //ex 17, #1
    @Test
    @Features(value={@Feature(value="Login user"), @Feature(value="Create user"), @Feature(value="Edit user")})
    @Description("This test failed edit user without auth")
    @DisplayName("Test negative edit user: user without auth")
    public void testEditUserWithoutAuthTest(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestWithJsonResponse("https://playground.learnqa.ru/api/user/", userData);

        String userId = responseCreateAuth.getString("id");

        // EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequestWithoutCookieAndToken("https://playground.learnqa.ru/api/user/"+userId,editData );

        Assertions.assertResponseTextEquals(responseEditUser,"Auth token not supplied" );
    }

    //ex 17, #2  v1 Редактируем, авторизовавшись под системным юзером
    @Test
    @Features(value={@Feature(value="Login user"), @Feature(value="Create user"), @Feature(value="Edit user")})
    @Description("This test failed edit user By system User")
    @DisplayName("test Edit Just Created Test By system User")
    public void testEditJustCreatedTestByAnotherUser(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestWithJsonResponse("https://playground.learnqa.ru/api/user/", userData);

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        // EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequestWithCookieAndToken("https://playground.learnqa.ru/api/user/" + userId, editData, this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid") );

        Assertions.assertResponseTextEquals(responseEditUser, "Please, do not edit test users with ID 1, 2, 3, 4 or 5.");
    }

    //ex 17, #2  v2 Редактируем, авторизовавшись под новым юзером
    @Test
    @Features(value={@Feature(value="Login user"), @Feature(value="Create user"), @Feature(value="Edit user")})
    @Description("This test failed edit user be another new user")
    @DisplayName("test Edit Just Created Test By another new User")
    public void testEditJustCreatedTestByAnotherUserV2(){
        //GENERATE  FIRST USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestWithJsonResponse("https://playground.learnqa.ru/api/user/", userData);

        String userId = responseCreateAuth.getString("id");

        //GENERATE  SECOND USER
        Map<String, String> userDataTwo = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuthTwo = apiCoreRequests
                .makePostRequestWithJsonResponse("https://playground.learnqa.ru/api/user/", userDataTwo);

        String userIdTwo = responseCreateAuthTwo.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        // EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequestWithCookieAndToken("https://playground.learnqa.ru/api/user/" + userIdTwo, editData, this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid") );

        Assertions.assertResponseCodeEquals(responseEditUser, 200);
    }


    //ex 17 #3
    @Test
    @Features(value={@Feature(value="Login user"), @Feature(value="Login user"), @Feature(value="Create user"), @Feature(value="Edit user")})
    @Description("This test failed edit user after auth, email without @")
    @DisplayName("Test negative edit user: email without @")
    public void testEditJustCreatedOnInvalidEmailTest(){
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

        // EDIT
        String newMail = DataGenerator.getRandomEmailWithoutAt();
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newMail);

        Response responseEditUser = apiCoreRequests
                .makePutRequestWithCookieAndToken("https://playground.learnqa.ru/api/user/" + userId, editData, this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Invalid email format");
    }

    //ex 17 #4
    @Test
    @Features(value={@Feature(value="Login user"), @Feature(value="Create user"), @Feature(value="Edit user")})
    @Description("This test failed edit user after auth, name is small")
    @DisplayName("Test negative edit user: name is small")
    public void testEditJustCreatedOnInvalidNameTest(){
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

        // EDIT
        String newName = DataGenerator.getRandomString(1,1);
        Map<String, String> editData = new HashMap<>();
        editData.put("name", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequestWithCookieAndToken("https://playground.learnqa.ru/api/user/" + userId, editData, this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "No data to update");
    }


}
