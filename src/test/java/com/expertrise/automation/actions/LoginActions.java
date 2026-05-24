package com.expertrise.automation.actions;

import com.expertrise.automation.pages.LoginPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

/**
 * LoginActions — business-level methods for login/signup workflows.
 *
 * <p>This layer sits between Step Definitions and Page Objects:
 * <ul>
 *   <li>Step Definitions call Actions methods (high-level business language)</li>
 *   <li>Actions call Page Object methods (element-level interactions)</li>
 *   <li>Actions contain assertions and flow logic</li>
 * </ul>
 *
 * <p>Pattern: Step → Actions → Page → WebDriver
 */
public class LoginActions {

    private static final Logger log = LogManager.getLogger(LoginActions.class);
    private final LoginPage loginPage;

    public LoginActions() {
        this.loginPage = new LoginPage();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // LOGIN FLOWS
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Performs a complete login flow: navigate → enter credentials → click login.
     *
     * @param email    registered email
     * @param password account password
     */
    public void login(String email, String password) {
        log.info("Performing login — email: {}", email);
        loginPage.navigateToLoginPage()
                 .enterLoginEmail(email)
                 .enterLoginPassword(password)
                 .clickLoginButton();
    }

    /**
     * Verifies that login was successful by checking the "Logged in as" badge.
     *
     * @param expectedUsername expected username in the nav badge
     */
    public void verifyLoginSuccess(String expectedUsername) {
        log.info("Verifying login success — expected username: {}", expectedUsername);
        Assert.assertTrue(loginPage.isLoggedIn(),
            "Login failed — 'Logged in as' badge not visible after login");
        if (expectedUsername != null && !expectedUsername.isBlank()) {
            String actual = loginPage.getLoggedInUsername();
            Assert.assertEquals(actual, expectedUsername,
                "Logged-in username mismatch");
        }
        log.info("Login verified successfully");
    }

    /**
     * Verifies that login failed and the correct error message is shown.
     *
     * @param expectedErrorText partial or full error message text
     */
    public void verifyLoginFailure(String expectedErrorText) {
        log.info("Verifying login failure — expected error: {}", expectedErrorText);
        Assert.assertTrue(loginPage.isLoginErrorDisplayed(),
            "Expected login error message not displayed");
        String actualError = loginPage.getLoginErrorText();
        Assert.assertTrue(actualError.toLowerCase().contains(expectedErrorText.toLowerCase()),
            "Error message mismatch. Expected to contain: '" + expectedErrorText +
            "' but got: '" + actualError + "'");
        log.info("Login failure verified — error: {}", actualError);
    }

    /**
     * Verifies login was unsuccessful and the user is NOT logged in.
     */
    public void verifyUserNotLoggedIn() {
        Assert.assertFalse(loginPage.isLoggedIn(),
            "User should NOT be logged in but 'Logged in as' badge is visible");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // SIGNUP FLOWS
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Enters signup name and email then clicks the Signup button.
     *
     * @param name  new user's full name
     * @param email new user's email
     */
    public void initiateSignup(String name, String email) {
        log.info("Initiating signup — name: {}, email: {}", name, email);
        loginPage.navigateToLoginPage()
                 .enterSignupName(name)
                 .enterSignupEmail(email)
                 .clickSignupButton();
    }

    /**
     * Verifies that an email-already-registered error is shown during signup.
     */
    public void verifyEmailAlreadyRegisteredError() {
        log.info("Verifying email already registered error");
        Assert.assertTrue(loginPage.isSignupErrorDisplayed(),
            "Expected 'email already registered' error not shown");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // LOGOUT
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Logs out the current user and verifies the login page is shown.
     */
    public void logout() {
        log.info("Performing logout");
        loginPage.clickLogout();
        Assert.assertTrue(loginPage.getCurrentUrl().contains("/login"),
            "Expected to be redirected to /login after logout");
        log.info("Logout successful");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // NAVIGATION HELPERS
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Navigates to the login page.
     */
    public void navigateToLoginPage() {
        loginPage.navigateToLoginPage();
    }

    /**
     * Returns the current page title.
     */
    public String getPageTitle() {
        return loginPage.getPageTitle();
    }

    /**
     * Returns the current URL.
     */
    public String getCurrentUrl() {
        return loginPage.getCurrentUrl();
    }
}
