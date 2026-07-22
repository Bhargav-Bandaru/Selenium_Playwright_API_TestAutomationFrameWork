package com.expertrise.automation.runners;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * JUnit 5 Cucumber Suite Runner.
 * ALL configuration lives in junit-platform.properties.
 * Do NOT add @ConfigurationParameter here — it overrides
 * command-line system properties and breaks tag filtering.
 *
 * Commands:
 *   mvn clean test                                        → all tests
 *   mvn clean test -Dcucumber.filter.tags="@smoke"        → smoke only
 *   mvn clean test -Dcucumber.filter.tags="@regression"   → regression only
 *   mvn clean test -Dcucumber.filter.tags="@api"          → API tests only
 *   mvn clean test -Dcucumber.filter.tags="@ui"           → UI tests only
 *   mvn clean test -Dcucumber.filter.tags="@smoke and @api" → smoke API only
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
public class RunCucumberTest {
    // Zero @ConfigurationParameter annotations here.
    // Any @ConfigurationParameter hardcoded here wins over
    // system properties — which breaks -Dcucumber.filter.tags
}