package com.expertrise.automation.runners;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.*;

/**
 * RunCucumberTest — JUnit 5 Suite runner for the full ExpertRise BDD framework.
 *
 * <p>Picks up all feature files from {@code src/test/resources/features}.
 * All Cucumber configuration lives in {@code junit-platform.properties}.</p>
 *
 * <p>Run commands:
 * <pre>
 *   mvn test                                          # all tests
 *   mvn test -Dcucumber.filter.tags="@smoke"          # smoke only
 *   mvn test -Dcucumber.filter.tags="@regression"     # regression only
 *   mvn test -Dcucumber.filter.tags="@api"            # API tests only
 *   mvn test -Dcucumber.filter.tags="@playwright"     # Playwright tests only
 *   mvn test -Dbrowser=firefox                        # override browser
 *   mvn test -Dheadless=true                          # headless mode (CI)
 * </pre>
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(
    key   = Constants.PLUGIN_PROPERTY_NAME,
    value = "pretty, " +
            "json:target/cucumber.json, " +
            "html:target/cucumber-reports.html, " +
            "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"   // Allure integration
)
@ConfigurationParameter(
    key   = Constants.GLUE_PROPERTY_NAME,
    value = "com.expertrise.automation.stepDefinitions," +
            "com.expertrise.automation.hooks"
)
@ConfigurationParameter(
        key   = Constants.FILTER_TAGS_PROPERTY_NAME,
        value = "@practice"
)
public class RunCucumberTest {
    // Annotations drive everything — no body needed
}
