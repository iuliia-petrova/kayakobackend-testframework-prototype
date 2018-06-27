package com.kayako.backendtest.oauth.auth;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

public class BasicAuthTests {

    private static final String TEST_USER_USERNAME = "test@test.com";
    private static final String TEST_USER_PASSWORD = "pass123";
    public static final String BASIC_ENCODED = "Basic a2F5YWtvYXBwOmtheWFrb3NlY3JldA==";
    private static final String TEST_USER_FIRSTNAME = "test_firstname";
    private static final String TEST_USER_LASTNAME = "test_lastname";
    private static final boolean TEST_USER_ACTIVE = true;
    //private static final String host = "http://localhost:8080";
    private static final String host = "http://dlb1.aureacentral.com:10203";
    public static final String OAUTH_TOKEN = "/oauth/token";
    public static final String USER = "/user";
    public static final String TWO_FA = "/oauth/2fa";

    String token;

    @Before
    public void setup(){
        getToken(getResponseWithToken(OAUTH_TOKEN));
    }

    public void shouldReturnErrorOnPostingCredentials() {
        getResponseWithToken(OAUTH_TOKEN).then().assertThat().statusCode(200);
    }

    @Test
    public void shouldReturnOkOnPostingCredentials() {
        getResponseWithToken(OAUTH_TOKEN).then().assertThat().statusCode(200);
    }

    @Test
    public void shouldReturnErrorOnPostingCredentialsWithBadToken() {
        getResponseWithToken(OAUTH_TOKEN).then().assertThat().statusCode(401);
    }

    @Test
    public void shouldReturnCurrenUserName() {

        given()
                .header("Authorization", "bearer " + token)
                .when().get(host +  USER)
                .then().assertThat().body("name", is(TEST_USER_USERNAME));
    }

    private Response getResponseWithToken(String url) {
        return given()
                .header("Content-Type", "application/x-www-form-urlencoded").and()
                .header("Authorization", BASIC_ENCODED)
                .formParam("grant_type", "password")
                .formParam("username", TEST_USER_USERNAME)
                .formParam("password", TEST_USER_PASSWORD)
                .formParam("scope", "read")
                .when().post(host + url)
                .andReturn();
    }

    private String getToken(Response response) {
        token = response.jsonPath().get("access_token");
        return token;
    }
}
