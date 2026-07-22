# ══════════════════════════════════════════════════════════════════════════════
# Feature: User API — REST CRUD lifecycle
# Target: http://localhost:3000/users  (json-server mock)
#
# This feature PRESERVES all original DummyAPIService.feature scenarios
# and extends them with additional API test coverage.
#
# Tags:
#   @api        — API test hook fires (no browser)
#   @mockService — original tag (preserved for backward compatibility)
#   @smoke      — critical path, run on every build
#   @regression — full regression suite
#   @crud       — CRUD operation tests
# ══════════════════════════════════════════════════════════════════════════════
@api
Feature: User API Lifecycle
  As a tester
  I want to validate the full lifecycle of a user via REST API
  So that I can ensure the API works correctly end-to-end

  # ────────────────────────────────────────────────────────────────────────────
  # ORIGINAL SCENARIO — preserved exactly from DummyAPIService.feature
  # ────────────────────────────────────────────────────────────────────────────

  @mockService @smoke @crud
  Scenario: Create, retrieve, update, and delete a user
    Given the API service is running
    When I create a new user with firstName "Jane", lastName "Doe", email "jane.doe@test.com", and role "user"
    Then the user is created successfully
    When I retrieve the created user
    Then the user details should match with email "jane.doe@test.com"
    When I update the user email to "jane.updated@test.com"
    Then the user email should be updated successfully
    When I delete the user
    Then the user should not be found

  # ────────────────────────────────────────────────────────────────────────────
  # EXTENDED API SCENARIOS
  # ────────────────────────────────────────────────────────────────────────────

  @regression
  Scenario: API returns 404 for non-existent user
    Given the API service is running
    When I request user with id "99999"
    Then the API response status code should be 404

  @regression @crud
  Scenario: Create user returns 201 with valid body
    Given the API service is running
    When I send a POST request to create user with body:
      """
      {
        "firstName": "Test",
        "lastName": "User",
        "email": "testuser@test.com",
        "role": "admin"
      }
      """
    Then the API response status code should be 201
    And the response body should contain field "id"
    And the response body field "email" should equal "testuser@test.com"

  @regression @crud
  Scenario: Update user with PATCH returns 200
    Given the API service is running
    And a user exists with email "existing@test.com"
    When I PATCH the user email to "updated@test.com"
    Then the API response status code should be 200
    And the response body field "email" should equal "updated@test.com"


  @regression @crud
  Scenario Outline: Create multiple users with different roles
    Given the API service is running
    When I create a new user with firstName "<firstName>", lastName "<lastName>", email "<email>", and role "<role>"
    Then the user is created successfully

    Examples:
      | firstName | lastName | email                   | role    |
      | Alice     | Smith    | alice.smith@test.com    | admin   |
      | Bob       | Jones    | bob.jones@test.com      | user    |
      | Charlie   | Brown    | charlie.brown@test.com  | viewer  |
