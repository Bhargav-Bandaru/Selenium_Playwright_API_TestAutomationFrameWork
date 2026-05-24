# ══════════════════════════════════════════════════════════════════════════════
# Feature: Playwright UI Automation — automationexercise.com
# Target: https://automationexercise.com/login
#
# These scenarios use Playwright instead of Selenium for cross-browser coverage.
# The @playwright tag triggers the Playwright hook in Hooks.java.
# ══════════════════════════════════════════════════════════════════════════════

Feature: Playwright Login Automation
  As a tester using Playwright
  I want to validate login flows with modern browser automation
  So that I have cross-browser coverage beyond Selenium


  Scenario: Playwright - Successful login with valid credentials
    Given the Playwright page is on the login screen
    When the user fills login email "[data-qa='login-email']" with "Bhargav@expertrise.com"
    And the user fills login password "[data-qa='login-password']" with "Test@1234"
    And the user clicks "[data-qa='login-button']"
    Then the Playwright page URL should contain "/dashboard"


  Scenario: Playwright - Login page title is correct
    Given the Playwright page is on the login screen
    Then the Playwright page title should contain "Automation Exercise"

  Scenario: Playwright - Invalid login shows error message
    Given the Playwright page is on the login screen
    When the user fills login email "[data-qa='login-email']" with "wrong@test.com"
    And the user fills login password "[data-qa='login-password']" with "wrongpass"
    And the user clicks "[data-qa='login-button']"
    Then the Playwright element ".login-form p" should be visible
