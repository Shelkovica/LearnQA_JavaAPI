package lib;

import io.qameta.allure.Step;

import io.restassured.response.Response;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {

    @Step("assert Json By Name: int value")
    public static void assertJsonByName(Response Response, String name, int expectedValue){
        Response.then().assertThat().body("$", hasKey(name));
        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    @Step("assert Json By Name: string value")
    public static void assertJsonByName(Response Response, String name, String expectedValue){
        Response.then().assertThat().body("$", hasKey(name));
        String value = Response.jsonPath().getString(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }
    @Step("assert Response Text Equals: string value")
    public static void assertResponseTextEquals(Response Response, String expectedAnswer){
        assertEquals(
                expectedAnswer,
                Response.asString(),
                "Response text is not as expected"
        );
    }
    @Step("assert Response Code Equals: int value")
    public static void assertResponseCodeEquals(Response Response, int expectedStatusCode){
        assertEquals(
                expectedStatusCode,
                Response.statusCode(),
                "Response status code  is not as expected"
        );
    }

    @Step("assert Json Has one Field")
    public static void assertJsonHasField(Response Response, String  expectedFieldName){
        Response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    @Step("assert Json Has Fields")
    public static void assertJsonHasFields(Response Response, String[]  expectedFieldNames){
        for (String expectedFieldName : expectedFieldNames){
            Assertions.assertJsonHasField(Response, expectedFieldName);
            }
    }

    @Step("assert Json Has Not Field")
    public static void assertJsonHasNotField(Response Response, String  unexpectedFieldName){
        Response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
    }
}
