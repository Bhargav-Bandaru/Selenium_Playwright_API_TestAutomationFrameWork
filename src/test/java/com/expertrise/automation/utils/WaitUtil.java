package com.expertrise.automation.utils;

import com.expertrise.automation.config.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

/**
 * WaitUtil — explicit wait helpers using WebDriverWait + ExpectedConditions.
 * Never use Thread.sleep() — always use these methods.
 */
public class WaitUtil {

    private static final Logger log = LogManager.getLogger(WaitUtil.class);
    private static final int DEFAULT_TIMEOUT = 15;
    private static final int POLLING_MS = 500;

    /** Returns a WebDriverWait with the default timeout. */
    public static WebDriverWait getWait() {
        return new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    /** Returns a WebDriverWait with a custom timeout in seconds. */
    public static WebDriverWait getWait(int timeoutSeconds) {
        return new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(timeoutSeconds));
    }

    /** Waits for element to be visible and returns it. */
    public static WebElement waitForVisible(By locator) {
        log.debug("Waiting for visible: {}", locator);
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Waits for element to be clickable and returns it. */
    public static WebElement waitForClickable(By locator) {
        log.debug("Waiting for clickable: {}", locator);
        return getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    /** Waits for element to be present in DOM (may be hidden). */
    public static WebElement waitForPresent(By locator) {
        return getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /** Waits for element to disappear (loader / spinner). */
    public static boolean waitForInvisible(By locator) {
        return getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /** Waits for URL to contain the given substring. */
    public static boolean waitForUrlContains(String urlFragment) {
        return getWait().until(ExpectedConditions.urlContains(urlFragment));
    }

    /** Waits for page title to contain the given text. */
    public static boolean waitForTitleContains(String titleFragment) {
        return getWait().until(ExpectedConditions.titleContains(titleFragment));
    }

    /** Waits for an alert to be present and returns it. */
    public static Alert waitForAlert() {
        return getWait().until(ExpectedConditions.alertIsPresent());
    }

    /**
     * Fluent wait — configurable polling, ignores StaleElementReferenceException.
     * Use for intermittent elements updated by AJAX.
     */
    public static WebElement fluentWait(By locator, int timeoutSeconds) {
        Wait<WebDriver> wait = new FluentWait<>(DriverFactory.getDriver())
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(POLLING_MS))
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class);
        return wait.until(driver -> driver.findElement(locator));
    }

    private WaitUtil() {}
}
