package com.expertrise.automation.utils;

import com.expertrise.automation.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * ApiUtility — reusable RestAssured helper methods for all API test scenarios.
 *
 * <p>Provides a clean, fluent interface wrapping RestAssured so step definitions
 * stay concise. Handles auth headers, base URI setup, logging, and common
 * assertion helpers.
 *
 * <p>Usage:
 * <pre>
 *   // GET
 *   Response response = ApiUtility.get("/users/1");
 *
 *   // POST with body
 *   Response response = ApiUtility.post("/users", requestBodyJson);
 *
 *   // With auth token
 *   Response response = ApiUtility.withAuth(token).get("/profile");
 *
 *   // Assertion helpers
 *   ApiUtility.assertStatusCode(response, 200);
 *   String email = ApiUtility.extractField(response, "email");
 * </pre>
 */
public class ApiUtility {

    private static final Logger log = LogManager.getLogger(ApiUtility.class);

    // Shared base request spec — built once, reused across all tests
    private static RequestSpecification baseRequestSpec;

    // ──────────────────────────────────────────────────────────────────────────
    // INITIALISATION
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Initialises RestAssured base URI and a shared RequestSpecification.
     * Call once in @BeforeAll or in the step that sets up the API service.
     *
     * @param baseUri base URI e.g. http://localhost:3000
     * @param basePath base path e.g. /users  (can be empty "")
     */
    public static void init(String baseUri, String basePath) {
        RestAssured.baseURI  = baseUri;
        RestAssured.basePath = basePath;

        baseRequestSpec = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setBasePath(basePath)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        log.info("RestAssured initialised — baseURI={}, basePath={}", baseUri, basePath);
    }

    /**
     * Initialises with default base URI from config.properties.
     */
    public static void initFromConfig() {
        init(ConfigManager.getApiBaseUrl(), "");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // HTTP METHODS — standard (no auth)
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * HTTP GET to the given endpoint.
     *
     * @param endpoint path appended to basePath e.g. "/1"
     * @return full RestAssured Response
     */
    public static Response get(String endpoint) {
        log.info("GET {}", endpoint);
        return given()
                .spec(getBaseSpec())
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * HTTP GET with query parameters.
     *
     * @param endpoint    path e.g. "/search"
     * @param queryParams map of query param key-value pairs
     */
    public static Response get(String endpoint, Map<String, Object> queryParams) {
        log.info("GET {} — queryParams={}", endpoint, queryParams);
        return given()
                .spec(getBaseSpec())
                .queryParams(queryParams)
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * HTTP POST with a JSON body string.
     *
     * @param endpoint    path e.g. "" (posts to basePath)
     * @param requestBody JSON string body
     */
    public static Response post(String endpoint, String requestBody) {
        log.info("POST {} — body={}", endpoint, requestBody);
        return given()
                .spec(getBaseSpec())
                .body(requestBody)
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * HTTP POST with a POJO body (serialised to JSON via Jackson).
     *
     * @param endpoint path
     * @param bodyPojo any Java object with Jackson annotations
     */
    public static Response post(String endpoint, Object bodyPojo) {
        log.info("POST {} — POJO body: {}", endpoint, bodyPojo.getClass().getSimpleName());
        return given()
                .spec(getBaseSpec())
                .body(bodyPojo)
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * HTTP PUT (full update) with a JSON body.
     */
    public static Response put(String endpoint, String requestBody) {
        log.info("PUT {} — body={}", endpoint, requestBody);
        return given()
                .spec(getBaseSpec())
                .body(requestBody)
                .when()
                .put(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * HTTP PATCH (partial update) with a JSON body.
     */
    public static Response patch(String endpoint, String requestBody) {
        log.info("PATCH {} — body={}", endpoint, requestBody);
        return given()
                .spec(getBaseSpec())
                .body(requestBody)
                .when()
                .patch(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * HTTP DELETE.
     */
    public static Response delete(String endpoint) {
        log.info("DELETE {}", endpoint);
        return given()
                .spec(getBaseSpec())
                .when()
                .delete(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // AUTHENTICATED REQUESTS — pass Bearer token
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Returns a RequestSpecification pre-loaded with a Bearer token.
     *
     * @param token Bearer token string (without "Bearer " prefix)
     */
    public static RequestSpecification withAuth(String token) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", "Bearer " + token);
    }

    /**
     * Returns a RequestSpecification with a custom header.
     */
    public static RequestSpecification withHeader(String name, String value) {
        return given().spec(getBaseSpec()).header(name, value);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // ASSERTION HELPERS
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Asserts the HTTP status code of a response.
     *
     * @param response         RestAssured response
     * @param expectedStatus   expected HTTP status code e.g. 200, 201, 404
     */
    public static void assertStatusCode(Response response, int expectedStatus) {
        int actualStatus = response.getStatusCode();
        if (actualStatus != expectedStatus) {
            throw new AssertionError(
                "Status code mismatch — expected: " + expectedStatus +
                " but got: " + actualStatus +
                " | Body: " + response.getBody().asString());
        }
        log.info("Status code assertion passed — {}", actualStatus);
    }

    /**
     * Extracts a field value from the JSON response body using JSONPath.
     *
     * @param response   RestAssured response
     * @param jsonPath   e.g. "id", "user.email", "items[0].name"
     */
    public static String extractField(Response response, String jsonPath) {
        String value = response.jsonPath().getString(jsonPath);
        log.info("Extracted '{}' = '{}'", jsonPath, value);
        return value;
    }

    /**
     * Extracts an integer field from the JSON response body.
     */
    public static int extractInt(Response response, String jsonPath) {
        return response.jsonPath().getInt(jsonPath);
    }

    /**
     * Returns the full response body as a String.
     */
    public static String getResponseBody(Response response) {
        return response.getBody().asString();
    }

    /**
     * Builds a standard response spec for common validations.
     */
    public static ResponseSpecification buildResponseSpec(int statusCode, String contentType) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .expectContentType(contentType)
                .build();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // INTERNAL HELPERS
    // ──────────────────────────────────────────────────────────────────────────

    private static RequestSpecification getBaseSpec() {
        if (baseRequestSpec == null) {
            initFromConfig();
        }
        return baseRequestSpec;
    }
}
