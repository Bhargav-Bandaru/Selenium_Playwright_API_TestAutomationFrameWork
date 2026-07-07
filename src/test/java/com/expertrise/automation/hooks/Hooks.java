package com.expertrise.automation.hooks;

import com.expertrise.automation.config.ConfigManager;
import com.expertrise.automation.config.DriverFactory;
import com.expertrise.automation.utils.ScreenshotUtil;
import com.expertrise.automation.utils.ExtentReportManager;
import io.cucumber.java.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hooks — Cucumber lifecycle hooks applied to every scenario.
 *
 * <p>Execution order:
 * <ol>
 *   <li>@Before  — init WebDriver (or Playwright), navigate to base URL</li>
 *   <li>Scenario steps run</li>
 *   <li>@AfterStep — log each step result</li>
 *   <li>@After   — screenshot on failure, quit driver, update Extent report</li>
 * </ol>
 *
 * <p>Tags control which hooks fire:
 * <ul>
 *   <li>{@code @ui}       — Selenium hook fires</li>
 *   <li>{@code @playwright} — Playwright hook fires</li>
 *   <li>{@code @api}      — API hook fires (no browser needed)</li>
 * </ul>
 */
public class Hooks {

    private static final Logger log = LogManager.getLogger(Hooks.class);

    // ──────────────────────────────────────────────────────────────────────────
    // SELENIUM — @ui and untagged scenarios (default UI)
    // ──────────────────────────────────────────────────────────────────────────

    /** Fires before every @ui scenario — initialises Selenium WebDriver. */
    @Before(value = "@ui or @login or @register or @products or @cart or @checkout or @practice", order = 1)
    public void beforeSeleniumScenario(Scenario scenario) {
        log.info(" BEFORE (Selenium) — Scenario: [{}]", scenario.getName());
        ExtentReportManager.createTest(scenario.getName(), String.join(", ", scenario.getSourceTagNames()));
        String browser = ConfigManager.getBrowser();
        DriverFactory.initSeleniumDriver(browser);
        DriverFactory.getDriver().get(ConfigManager.getBaseUrl());
        log.info("Navigated to: {}", ConfigManager.getBaseUrl());
    }

    /** Fires after every @ui scenario — screenshot on fail, quit driver. */
    @After(value = "@ui or @login or @register or @products or @cart or @checkout or @practice", order = 1)
    public void afterSeleniumScenario(Scenario scenario) {
        log.info("AFTER (Selenium) — Scenario: [{}] Status: {}", scenario.getName(), scenario.getStatus());
        try {
            if (scenario.isFailed()) {
                log.error("Scenario FAILED — capturing screenshot");
                byte[] screenshot = ScreenshotUtil.captureSeleniumScreenshot(DriverFactory.getDriver());
                if (screenshot != null) {
                    scenario.attach(screenshot, "image/png", "Failure Screenshot — " + scenario.getName());
                    ExtentReportManager.attachScreenshotOnFailure(scenario.getName(), screenshot);

                }
                ExtentReportManager.failTest(scenario.getName());
            } else {
                ExtentReportManager.passTest(scenario.getName());
            }
        } finally {
            DriverFactory.quitDriver();
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // PLAYWRIGHT — @playwright scenarios
    // ──────────────────────────────────────────────────────────────────────────

    /** Fires before every @playwright scenario — initialises Playwright Page. */
/*    @Before(value = "@playwright", order = 1)
    public void beforePlaywrightScenario(Scenario scenario) {
        log.info("▶ BEFORE (Playwright) — Scenario: [{}]", scenario.getName());
        ExtentReportManager.createTest(scenario.getName(), String.join(", ", scenario.getSourceTagNames()));
        String browser = ConfigManager.get("playwright.browser", "chromium");
        DriverFactory.initPlaywrightDriver(browser);
        DriverFactory.getPlaywrightPage().navigate(ConfigManager.getBaseUrl());
        log.info("Playwright navigated to: {}", ConfigManager.getBaseUrl());
    }*/

    /** Fires after every @playwright scenario — screenshot on fail, close browser. */
    /*@After(value = "@playwright", order = 1)
    public void afterPlaywrightScenario(Scenario scenario) {
        log.info("■ AFTER (Playwright) — Scenario: [{}] Status: {}", scenario.getName(), scenario.getStatus());
        try {
            if (scenario.isFailed()) {
                log.error("Playwright Scenario FAILED — capturing screenshot");
                byte[] screenshot = DriverFactory.getPlaywrightPage()
                        .screenshot(new com.microsoft.playwright.Page.ScreenshotOptions().setFullPage(true));
                if (screenshot != null) {
                    scenario.attach(screenshot, "image/png", "Playwright Failure — " + scenario.getName());
                    ExtentReportManager.attachScreenshotOnFailure(scenario.getName(), screenshot);
                }
                ExtentReportManager.failTest(scenario.getName());
            } else {
                ExtentReportManager.passTest(scenario.getName());
            }
        } finally {
            DriverFactory.quitPlaywright();
        }
    }
*/
    // ──────────────────────────────────────────────────────────────────────────
    // API — @api scenarios (no browser, just logging)
    // ──────────────────────────────────────────────────────────────────────────

    /** Fires before every @api / @mockService scenario. */
    @Before(value = "@api or @mockService", order = 1)
    public void beforeApiScenario(Scenario scenario) {
        log.info("▶ BEFORE (API) — Scenario: [{}]", scenario.getName());
        ExtentReportManager.createTest(scenario.getName(), String.join(", ", scenario.getSourceTagNames()));
    }

    /** Fires after every @api / @mockService scenario. */
    @After(value = "@api or @mockService", order = 1)
    public void afterApiScenario(Scenario scenario) {
        log.info("■ AFTER (API) — Scenario: [{}] Status: {}", scenario.getName(), scenario.getStatus());
        if (scenario.isFailed()) {
            ExtentReportManager.failTest(scenario.getName());
        } else {
            ExtentReportManager.passTest(scenario.getName());
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // GLOBAL — fires for EVERY scenario regardless of tag
    // ──────────────────────────────────────────────────────────────────────────

    /** Flush Extent Report after every scenario — ensures partial runs are saved. */
    @After(order = 0)  // order 0 = last to run
    public void flushReports() {
        ExtentReportManager.flush();
    }
}
