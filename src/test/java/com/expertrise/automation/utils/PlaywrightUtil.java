package com.expertrise.automation.utils;

import com.expertrise.automation.config.DriverFactory;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;
import java.util.List;

/**
 * PlaywrightUtil — reusable Playwright interaction helpers.
 *
 * <p>Provides a clean API over raw Playwright so step definitions and
 * action classes don't need to deal with Playwright specifics.</p>
 *
 * <p>Usage:
 * <pre>
 *   PlaywrightUtil.navigate("https://example.com");
 *   PlaywrightUtil.click("[data-qa='login-button']");
 *   PlaywrightUtil.fill("[data-qa='login-email']", "user@test.com");
 *   PlaywrightUtil.takeScreenshot("step1_login");
 *   boolean visible = PlaywrightUtil.isVisible(".error-message");
 * </pre>
 */
public class PlaywrightUtil {

    private static final Logger log = LogManager.getLogger(PlaywrightUtil.class);
    private static final int DEFAULT_TIMEOUT = 30_000; // 30 seconds

    // ──────────────────────────────────────────────────────────────────────────
    // NAVIGATION
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Navigates the current Playwright Page to the given URL.
     */
    public static void navigate(String url) {
        log.info("Playwright navigating to: {}", url);
        getPage().navigate(url);
        getPage().waitForLoadState(LoadState.NETWORKIDLE);
    }

    /**
     * Reloads the current page.
     */
    public static void reload() {
        getPage().reload();
        getPage().waitForLoadState(LoadState.NETWORKIDLE);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // ELEMENT INTERACTIONS
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Clicks an element matching the given CSS/XPath selector.
     *
     * @param selector CSS or XPath selector
     */
    public static void click(String selector) {
        log.info("Playwright click: {}", selector);
        getPage().locator(selector).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(DEFAULT_TIMEOUT));
        getPage().locator(selector).click();
    }

    /**
     * Fills a text input field (clears first then types).
     *
     * @param selector CSS/XPath selector for the input
     * @param value    text to enter
     */
    public static void fill(String selector, String value) {
        log.info("Playwright fill: {} = '{}'", selector, value);
        Locator el = getPage().locator(selector);
        el.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(DEFAULT_TIMEOUT));
        el.clear();
        el.fill(value);
    }

    /**
     * Selects an option in a &lt;select&gt; dropdown by visible text.
     *
     * @param selector   CSS selector for the select element
     * @param optionText visible text of the option
     */
    public static void selectOption(String selector, String optionText) {
        log.info("Playwright selectOption: {} = '{}'", selector, optionText);
        getPage().locator(selector).selectOption(new com.microsoft.playwright.options.SelectOption()
                .setLabel(optionText));
    }

    /**
     * Checks or unchecks a checkbox.
     *
     * @param selector CSS/XPath for the checkbox
     * @param check    true to check, false to uncheck
     */
    public static void setCheckbox(String selector, boolean check) {
        Locator el = getPage().locator(selector);
        if (check) el.check(); else el.uncheck();
    }

    /**
     * Hovers the mouse over an element.
     */
    public static void hover(String selector) {
        getPage().locator(selector).hover();
    }

    /**
     * Presses a keyboard key on a focused element.
     * @param selector  element to focus
     * @param key       key name e.g. "Enter", "Tab", "Escape"
     */
    public static void pressKey(String selector, String key) {
        getPage().locator(selector).press(key);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // STATE / RETRIEVAL
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Gets the visible text of an element.
     *
     * @param selector CSS/XPath selector
     */
    public static String getText(String selector) {
        return getPage().locator(selector).innerText().trim();
    }

    /**
     * Gets the value attribute of an input field.
     */
    public static String getValue(String selector) {
        return getPage().locator(selector).inputValue();
    }

    /**
     * Gets an attribute value of an element.
     */
    public static String getAttribute(String selector, String attribute) {
        return getPage().locator(selector).getAttribute(attribute);
    }

    /**
     * Returns true if the element is visible on the page.
     */
    public static boolean isVisible(String selector) {
        try {
            return getPage().locator(selector).isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if the element exists in the DOM (may be hidden).
     */
    public static boolean isPresent(String selector) {
        return getPage().locator(selector).count() > 0;
    }

    /**
     * Returns all matching elements' text as a List.
     */
    public static List<String> getAllTexts(String selector) {
        return getPage().locator(selector).allInnerTexts();
    }

    /**
     * Returns the current page URL.
     */
    public static String getCurrentUrl() {
        return getPage().url();
    }

    /**
     * Returns the current page title.
     */
    public static String getTitle() {
        return getPage().title();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // WAITING
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Waits for an element to be visible.
     *
     * @param selector       CSS/XPath
     * @param timeoutMillis  max wait time in ms
     */
    public static void waitForVisible(String selector, int timeoutMillis) {
        getPage().locator(selector).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(timeoutMillis));
    }

    /**
     * Waits for navigation to complete (networkidle).
     */
    public static void waitForNavigation() {
        getPage().waitForLoadState(LoadState.NETWORKIDLE);
    }

    /**
     * Waits for a URL pattern to match.
     *
     * @param urlPattern partial URL string to wait for
     */
    public static void waitForUrl(String urlPattern) {
        getPage().waitForURL("**" + urlPattern + "**");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // SCREENSHOTS
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Takes a full-page screenshot and saves it to target/screenshots/.
     *
     * @param fileName screenshot file name (without extension)
     * @return byte[] screenshot data (also used for Cucumber report attachment)
     */
    public static byte[] takeScreenshot(String fileName) {
        String path = "target/screenshots/playwright_" + fileName + "_"
                + System.currentTimeMillis() + ".png";
        byte[] bytes = getPage().screenshot(new Page.ScreenshotOptions()
                .setFullPage(true)
                .setPath(Paths.get(path)));
        log.info("Playwright screenshot saved: {}", path);
        return bytes;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // INTERNAL
    // ──────────────────────────────────────────────────────────────────────────

    private static Page getPage() {
        return DriverFactory.getPlaywrightPage();
    }

    private PlaywrightUtil() { /* utility class */ }
}
