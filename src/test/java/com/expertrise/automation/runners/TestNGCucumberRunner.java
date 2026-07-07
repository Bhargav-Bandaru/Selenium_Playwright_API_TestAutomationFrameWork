package com.expertrise.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.expertrise.automation.stepDefinitions"},
        plugin = {"pretty", "html:target/cucumber-reports.html", "json:target/cucumber.json"},
        tags = "@practice or @dropdown"
)
public class TestNGCucumberRunner extends AbstractTestNGCucumberTests {
    // No code needed here – inheritance handles execution
}