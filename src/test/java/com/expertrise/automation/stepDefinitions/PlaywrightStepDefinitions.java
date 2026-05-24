package com.expertrise.automation.stepDefinitions;

import com.expertrise.automation.utils.PlaywrightUtil;
import io.cucumber.java.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;

/**
 * PlaywrightStepDefinitions — Playwright-specific step definitions.
 * Generic enough to be reused across any Playwright feature file.
 */
public class PlaywrightStepDefinitions {

    private static final Logger log = LogManager.getLogger(PlaywrightStepDefinitions.class);

    @Given("the Playwright page is on the login screen")
    public void playwrightOnLoginScreen() {
        log.info("Step: Playwright page is on the login screen");
        // navigation handled by @Before hook; just verify login form is visible
        PlaywrightUtil.waitForVisible("[data-qa='login-email']", 15_000);
    }

    @When("the user fills login email {string} with {string}")
    public void fillLoginEmail(String selector, String value) {
        log.info("Step: filling {} = '{}'", selector, value);
        PlaywrightUtil.fill(selector, value);
    }

    @When("the user fills login password {string} with {string}")
    public void fillLoginPassword(String selector, String value) {
        log.info("Step: filling password selector={}", selector);
        PlaywrightUtil.fill(selector, value);
    }

    @When("the user clicks {string}")
    public void clickElement(String selector) {
        log.info("Step: clicking '{}'", selector);
        PlaywrightUtil.click(selector);
        PlaywrightUtil.waitForNavigation();
    }

    @Then("the Playwright page URL should contain {string}")
    public void verifyUrlContains(String urlFragment) {
        log.info("Step: verifying URL contains '{}'", urlFragment);
        String currentUrl = PlaywrightUtil.getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains(urlFragment),
            "URL should contain '" + urlFragment + "' but was: " + currentUrl);
    }

    @Then("the Playwright page title should contain {string}")
    public void verifyTitleContains(String titleFragment) {
        log.info("Step: verifying title contains '{}'", titleFragment);
        String title = PlaywrightUtil.getTitle();
        Assertions.assertTrue(title.contains(titleFragment),
            "Page title should contain '" + titleFragment + "' but was: " + title);
    }

    @Then("the Playwright element {string} should be visible")
    public void verifyElementVisible(String selector) {
        log.info("Step: verifying element visible: {}", selector);
        Assertions.assertTrue(PlaywrightUtil.isVisible(selector),
            "Element '" + selector + "' should be visible but is not");
    }

    @Then("the Playwright element {string} text should contain {string}")
    public void verifyElementText(String selector, String expectedText) {
        String actual = PlaywrightUtil.getText(selector);
        Assertions.assertTrue(actual.contains(expectedText),
            "Element text should contain '" + expectedText + "' but got: '" + actual + "'");
    }
}
