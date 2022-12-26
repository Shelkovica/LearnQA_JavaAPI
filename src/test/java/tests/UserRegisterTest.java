package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import lib.ApiCoreRequests;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {

    private  final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("This test fail create user with existing email ")
    @DisplayName("Test create user with existing email")
    public void testCreateUserWithExistingEmail(){
        Map<String, String> userData = new HashMap<>();
        String email = "vinkotov@example.com";
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    }

    @Test
    @Description("This test successfully create user ")
    @DisplayName("Test positive create user")
    public void testCreateUserSuccessfully(){
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");

    }

    //Ex15
    @Test
    @Description("This test fail create user with incorrect email")
    @DisplayName("Test negative create user")
    public void testCreateUserWithIncorrectEmail(){
        Map<String, String> userData = DataGenerator.getIncorrectRegistrationData();
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
    }


    @ParameterizedTest
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
    @Description("This test fail create user without one parameter")
    @DisplayName("Test negative create user,without one parameter")
    public void testCreateUserWithoutOneParameter(String condition){

        Map<String, String> userData = new HashMap<>();
        userData.put(condition, condition);
        userData = DataGenerator.getRegistrationDataWithoutSomeField(userData);
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: "+condition);
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    }

    @Test
    @Description("This test fail create user with small name")
    @DisplayName("Test negative create user with small name")
    public void testCreateUserWithSmallName(){
         Map<String, String> userData = DataGenerator.getRegistrationDataWithSmallName();
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too short");
    }

    @Test
    @Description("This test fail create user with large name")
    @DisplayName("Test negative create user with large name")
    public void testCreateUserWithLargeName(){
        Map<String, String> userData = DataGenerator.getRegistrationDataWithLargeName();
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too long");
    }
}
