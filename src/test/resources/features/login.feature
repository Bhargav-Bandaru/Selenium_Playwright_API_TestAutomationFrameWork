# ══════════════════════════════════════════════════════════════════════════════
# Feature: Login & Authentication — automationexercise.com/login
# Target: https://automationexercise.com/login
# DOM Source: AutomationExercise.html (data-qa locators)
#
# Tags:
#   @ui        — Selenium WebDriver hook fires
#   @smoke     — critical path tests, run on every build
#   @regression — full regression suite
#   @login     — login-specific scenarios
#   @negative  — negative/invalid input tests
# ══════════════════════════════════════════════════════════════════════════════
@ui @login
Feature: Login and Authentication
  As a registered user of AutomationExercise
  I want to be able to login and logout securely
  So that I can access my account and manage my data

  Background:
    Given the user is on the login page

  # ────────────────────────────────────────────────────────────────────────────
  # HAPPY PATH
  # ────────────────────────────────────────────────────────────────────────────

  @smoke @regression
  Scenario: Successful login with valid credentials
    When the user enters email "expert@test.com" and password "expert@123"
    Then the user should be logged in successfully
    And the page title should contain "Automation Exercise"

  @regression
  Scenario: Login verifies the correct username in navigation
    When the user enters email "expert@test.com" and password "expert@test.com"
    Then the user should be logged in as "Bhargav"

#  @smoke @regression
#  Scenario: User can log out after logging in
#    Given the user is logged in with email "Bhargav@expertrise.com" and password "Test@1234"
#    When the user clicks logout
#    Then the user should be redirected to the login page
#    And the user should not be logged in

  # ────────────────────────────────────────────────────────────────────────────
  # DATA-DRIVEN — multiple credential sets
  # ────────────────────────────────────────────────────────────────────────────

  @regression
  Scenario Outline: Login with multiple valid user accounts
    When the user enters email "<email>" and password "<password>"
    Then the user should be logged in successfully

    Examples:
      | email           | password   |
      | expert@test.com | expert@123 |


  # ────────────────────────────────────────────────────────────────────────────
  # NEGATIVE TESTS
  # ────────────────────────────────────────────────────────────────────────────

  @negative @regression
  Scenario: Login fails with incorrect password
    When the user enters email "bhargav@test.com" and password "WrongPassword123"
    Then the login should fail with error "Your email or password is incorrect"
    And the user should not be logged in

  @negative @regression
  Scenario: Login fails with unregistered email
    When the user enters email "notexist@random.com" and password "Test@1234"
    Then the login should fail with error "Your email or password is incorrect"

  @negative @regression
  Scenario: Login fails with empty email and password
    When the user enters email "" and password ""
    Then the user should not be logged in

  @negative @regression
  Scenario: Login fails with invalid email format
    When the user enters email "not-an-email" and password "Test@1234"
    Then the user should not be logged in

  # ────────────────────────────────────────────────────────────────────────────
  # SIGNUP SCENARIOS
  # ────────────────────────────────────────────────────────────────────────────

  @regression
  Scenario: New user signup redirects to account creation
    When the user enters signup name "Jane Doe" and email "janedoe.new@test.com"
    Then the page title should contain "Automation Exercise"

  @negative @regression
  Scenario: Signup with already registered email shows error
    When the user enters signup name "Existing User" and email "bhargav@test.com"
    Then the email already registered error should be shown
