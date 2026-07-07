package com.expertrise.automation.stepDefinitions;

import com.expertrise.automation.actions.LoginActions;
import com.expertrise.automation.utils.TestDataUtil;
import io.cucumber.java.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

/**
 * LoginStepDefinitions — binds Gherkin steps in login.feature to LoginActions.
 *
 * <p>This class ONLY contains Cucumber annotations + calls to Actions methods.
 * No direct WebDriver or Page Object calls — all logic is in Actions layer.</p>
 */
public class LoginStepDefinitions {

    private static final Logger log = LogManager.getLogger(LoginStepDefinitions.class);

    private final LoginActions loginActions = new LoginActions();

    // ──────────────────────────────────────────────────────────────────────────
    // GIVEN — pre-conditions
    // ──────────────────────────────────────────────────────────────────────────

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        log.info("Step: the user is on the login page");
        loginActions.navigateToLoginPage();
    }

    @Given("the user is logged in with email {string} and password {string}")
    public void theUserIsLoggedIn(String email, String password) {
        log.info("Step: the user is logged in with email {}", email);
        loginActions.login(email, password);
        loginActions.verifyLoginSuccess(null);  // just verify logged-in, not username
    }

    // ──────────────────────────────────────────────────────────────────────────
    // WHEN — actions
    // ──────────────────────────────────────────────────────────────────────────

    @When("the user enters email {string} and password {string}")
    public void theUserEntersEmailAndPassword(String email, String password) {
        log.info("Step: entering email={}, password=****", email);
        loginActions.login(email, password);
    }

    @When("the user enters valid credentials from test data {string}")
    public void theUserEntersValidCredentialsFromTestData(String dataKey) {
        log.info("Step: loading credentials for key={}", dataKey);
        String email    = TestDataUtil.get(dataKey + ".email");
        String password = TestDataUtil.get(dataKey + ".password");
        loginActions.login(email, password);
    }

    @When("the user enters signup name {string} and email {string}")
    public void theUserEntersSignupNameAndEmail(String name, String email) {
        log.info("Step: signup — name={}, email={}", name, email);
        loginActions.initiateSignup(name, email);
    }

    @When("the user clicks logout")
    public void theUserClicksLogout() {
        log.info("Step: the user clicks logout");
        loginActions.logout();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // THEN — verifications
    // ──────────────────────────────────────────────────────────────────────────

    @Then("the user should be logged in successfully")
    public void theUserShouldBeLoggedInSuccessfully() {
        log.info("Step: verifying login success");
        loginActions.verifyLoginSuccess(null);
    }

    @Then("the user should be logged in as {string}")
    public void theUserShouldBeLoggedInAs(String username) {
        log.info("Step: verifying logged in as '{}'", username);
        loginActions.verifyLoginSuccess(username);
    }

    @Then("the login should fail with error {string}")
    public void theLoginShouldFailWithError(String errorText) {
        log.info("Step: verifying login failure with error '{}'", errorText);
        loginActions.verifyLoginFailure(errorText);
    }

    @Then("the user should not be logged in")
    public void theUserShouldNotBeLoggedIn() {
        log.info("Step: verifying user is not logged in");
        loginActions.verifyUserNotLoggedIn();
    }

    @Then("the user should be redirected to the login page")
    public void theUserShouldBeRedirectedToLoginPage() {
        log.info("Step: verifying redirect to login page");
        String url = loginActions.getCurrentUrl();
        Assert.assertTrue(url.contains("/login"),
            "Expected URL to contain '/login' but got: " + url);
    }

    @Then("the page title should contain {string}")
    public void thePageTitleShouldContain(String expectedTitle) {
        log.info("Step: verifying page title contains '{}'", expectedTitle);
        String actual = loginActions.getPageTitle();
        Assert.assertTrue(actual.contains(expectedTitle),
            "Page title mismatch. Expected to contain: '" + expectedTitle +
            "' but got: '" + actual + "'");
    }

    @Then("the email already registered error should be shown")
    public void theEmailAlreadyRegisteredErrorShouldBeShown() {
        log.info("Step: verifying email already registered error");
        loginActions.verifyEmailAlreadyRegisteredError();
    }


}
