package com.expertrise.automation.stepDefinitions;

import com.expertrise.automation.utils.ApiUtility;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * ApiStepDefinitions — step definitions for api.feature.
 *
 * <p>Preserves ALL original DummyUserStepDefinitions steps and adds
 * new steps for extended API scenarios.</p>
 */
public class ApiStepDefinitions {

    private static final Logger log = LogManager.getLogger(ApiStepDefinitions.class);

    private static String createdUserId;
    private Response response;

    // ──────────────────────────────────────────────────────────────────────────
    // ORIGINAL STEPS — preserved from DummyUserStepDefinitions
    // ──────────────────────────────────────────────────────────────────────────

    @Given("the API service is running")
    public void apiServiceRunning() {
        RestAssured.baseURI  = "http://localhost:3000";
        RestAssured.basePath = "/users";
        RestAssured.requestSpecification = given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
        log.info("API service configured at {}{}", RestAssured.baseURI, RestAssured.basePath);
    }

    @When("I create a new user with firstName {string}, lastName {string}, email {string}, and role {string}")
    public void createUser(String firstName, String lastName, String email, String role) {
        String requestBody = String.format(
            "{ \"firstName\": \"%s\", \"lastName\": \"%s\", \"email\": \"%s\", \"role\": \"%s\" }",
            firstName, lastName, email, role);

        response = given()
                .body(requestBody)
                .when().post()
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract().response();

        createdUserId = response.jsonPath().getString("id");
        log.info("Created user id: {}", createdUserId);
    }

    @Then("the user is created successfully")
    public void verifyUserCreated() {
        Assertions.assertNotNull(createdUserId, "User ID should not be null after creation");
        log.info("Verified user created — id: {}", createdUserId);
    }

    @When("I retrieve the created user")
    public void getUser() {
        response = given()
                .when().get("/" + createdUserId)
                .then().statusCode(200)
                .extract().response();
    }

    @Then("the user details should match with email {string}")
    public void verifyUserDetails(String email) {
        Assertions.assertEquals(createdUserId, response.jsonPath().getString("id"));
        Assertions.assertEquals(email, response.jsonPath().getString("email"),
            "Email mismatch in retrieved user");
    }

    @When("I update the user email to {string}")
    public void updateUser(String newEmail) {
        response = given()
                .body("{ \"email\": \"" + newEmail + "\" }")
                .when().patch("/" + createdUserId)
                .then().statusCode(200)
                .extract().response();
    }

    @Then("the user email should be updated successfully")
    public void verifyUserUpdated() {
        Assertions.assertTrue(response.jsonPath().getString("email").contains("updated"),
            "Email should contain 'updated' after PATCH");
    }

    @When("I delete the user")
    public void deleteUser() {
        given().when().delete("/" + createdUserId).then().statusCode(200);
    }

    @Then("the user should not be found")
    public void verifyUserDeleted() {
        given().when().get("/" + createdUserId).then().statusCode(404);
        log.info("Verified user deleted — 404 confirmed for id: {}", createdUserId);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // EXTENDED API STEPS
    // ──────────────────────────────────────────────────────────────────────────

    @When("I request user with id {string}")
    public void requestUserById(String userId) {
        log.info("Requesting user id: {}", userId);
        response = given().when().get("/" + userId).then().extract().response();
    }

    @When("I send a POST request to create user with body:")
    public void sendPostWithBody(String requestBody) {
        log.info("POST with body: {}", requestBody);
        response = given()
                .body(requestBody)
                .when().post()
                .then().extract().response();

        String id = response.jsonPath().getString("id");
        if (id != null) createdUserId = id;
    }

    @When("I send a GET request to {string}")
    public void sendGet(String endpoint) {
        log.info("GET {}", endpoint);
       // RestAssured.basePath = "";
        response = given().when().get(endpoint).then().extract().response();
    }

    @Given("a user exists with email {string}")
    public void aUserExistsWithEmail(String email) {
        String body = "{ \"firstName\": \"Test\", \"lastName\": \"Existing\", " +
                      "\"email\": \"" + email + "\", \"role\": \"user\" }";
        response = given().body(body).when().post().then().statusCode(201).extract().response();
        createdUserId = response.jsonPath().getString("id");
        log.info("Pre-existing user created — id: {}", createdUserId);
    }

    @When("I PATCH the user email to {string}")
    public void patchUserEmail(String newEmail) {
        response = given()
                .body("{ \"email\": \"" + newEmail + "\" }")
                .when().patch("/" + createdUserId)
                .then().extract().response();
    }

    @Then("the API response status code should be {int}")
    public void verifyStatusCode(int expectedStatus) {
        int actual = response.getStatusCode();
        Assertions.assertEquals(expectedStatus, actual,
            "Status code mismatch — expected: " + expectedStatus + " got: " + actual);
        log.info("Status code verified: {}", actual);
    }

    @Then("the response body should contain field {string}")
    public void verifyFieldExists(String fieldName) {
        String value = response.jsonPath().getString(fieldName);
        Assertions.assertNotNull(value, "Field '" + fieldName + "' should exist in response body");
    }

    @Then("the response body field {string} should equal {string}")
    public void verifyFieldEquals(String fieldName, String expectedValue) {
        String actual = response.jsonPath().getString(fieldName);
        Assertions.assertEquals(expectedValue, actual,
            "Field '" + fieldName + "' expected: '" + expectedValue + "' but got: '" + actual + "'");
    }

    @Then("the response should contain a list of users")
    public void verifyUserList() {
        int count = response.jsonPath().getList("$").size();
        Assertions.assertTrue(count >= 0, "Response should be a list (got " + count + " items)");
        log.info("User list returned — {} items", count);
    }
}
