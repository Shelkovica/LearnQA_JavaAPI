package lib;

import javax.xml.crypto.Data;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;


public class DataGenerator {

    @Step("get Random Email")
    public static String getRandomEmail(){
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "learqa" + timestamp + "@example.com";
    }

    @Step("get Random String")
    public static String getRandomString(Integer len, Integer len_end){
        String random_string = RandomStringUtils.randomAlphabetic(len, len_end);
        return random_string;
    }

    @Step("get Random Email Without At")
    public static String getRandomEmailWithoutAt(){
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "learqa" + timestamp + "example.com";
    }

    @Step("get Registration Data")
    public static Map<String, String> getRegistrationData(){
        Map<String, String> data = new HashMap<>();
        data.put("email", DataGenerator.getRandomEmail());
        data.put("password", "123");
        data.put("username", "learnqa");
        data.put("firstName", "learnqa");
        data.put("lastName", "learnqa");
        return data;
    }

    @Step("get Registration Random Data")
    public static Map<String, String> getRegistrationRandomData(){
        Map<String, String> data = new HashMap<>();
        String random_string = "learqa " + DataGenerator.getRandomString(2, 2);
        data.put("email", DataGenerator.getRandomEmail());
        data.put("password", random_string);
        data.put("username", random_string);
        data.put("firstName", random_string);
        data.put("lastName", random_string);
        return data;
    }

    @Step("get Incorrect Registration Data")
    public static Map<String, String> getIncorrectRegistrationData(){
        Map<String, String> data = new HashMap<>();
        data.put("email", DataGenerator.getRandomEmailWithoutAt());
        data.put("password", "123");
        data.put("username", "learnqa");
        data.put("firstName", "learnqa");
        data.put("lastName", "learnqa");
        return data;
    }

    @Step("get Registration Data non Default Values")
    public static Map<String, String> getRegistrationData(Map<String, String> nonDefaultValues){
        Map<String, String> defaultValues = DataGenerator.getRegistrationData();
        Map<String, String> userData = new HashMap<>();
        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        for (String key: keys){
            if (nonDefaultValues.containsKey(key)){
                userData.put(key, nonDefaultValues.get(key));
            } else {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }

    @Step("get Registration Data Without Some Field")
    public static Map<String, String> getRegistrationDataWithoutSomeField(Map<String, String> EmptyValues){
        Map<String, String> defaultValues = DataGenerator.getRegistrationRandomData();
        Map<String, String> userData = new HashMap<>();
        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        for (String key: keys){
            if (EmptyValues.containsKey(key)){
                userData.put(key, null);
            } else {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }

    @Step("get Registration Data With Small Name")
    public static Map<String, String> getRegistrationDataWithSmallName(){
        Map<String, String> data = new HashMap<>();
        String random_small_name = DataGenerator.getRandomString(1,1);
        String random_string = "learqa " +  DataGenerator.getRandomString(2,5);
        data.put("email", DataGenerator.getRandomEmail());
        data.put("password", random_string);
        data.put("username", random_small_name);
        data.put("firstName", random_string);
        data.put("lastName", random_string);
        return data;
    }

    @Step("get Registration Data With Large Name")
    public static Map<String, String> getRegistrationDataWithLargeName(){
        Map<String, String> data = new HashMap<>();
        String random_small_name = DataGenerator.getRandomString(250,300);
        String random_string = "learqa " +  DataGenerator.getRandomString(2,5);
        data.put("email", DataGenerator.getRandomEmail());
        data.put("password", random_string);
        data.put("username", random_small_name);
        data.put("firstName", random_string);
        data.put("lastName", random_string);
        return data;
    }

}
