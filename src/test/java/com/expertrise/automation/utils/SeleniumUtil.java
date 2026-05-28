package com.expertrise.automation.utils;

import com.expertrise.automation.config.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * SeleniumUtil — complete reusable Selenium WebDriver helper library.
 *
 * Categories covered:
 *   Click           — clickElement, jsClick, doubleClick, rightClick
 *   Input           — typeText, clearField, appendText, pressKey
 *   Dropdown        — selectByVisibleText/Value/Index, getAllDropdownOptions
 *   Frame           — switchToFrame, switchToDefaultContent, switchToParentFrame
 *   Alert           — acceptAlert, dismissAlert, getAlertText, typeInAlert
 *   Window/Tab      — switchToNewWindow, switchToWindow, getWindowCount
 *   Scroll          — scrollIntoView, scrollToTop, scrollToBottom, scrollByPixels
 *   Drag-Drop       — dragAndDrop, dragAndDropByOffset
 *   Hover/Mouse     — hoverOverElement, hoverAndClick
 *   State checks    — isDisplayed, isEnabled, isSelected, isPresent
 *   Text/Attribute  — getText, getAttribute, getInputValue, getAllTexts, getElementCount
 *   JavaScript      — jsClick, executeScript, highlightElement, setValueByJS, waitForPageLoad
 *
 * FIX APPLIED:
 *   Private helper method renamed from wait() → getWait().
 *   Reason: Every Java class inherits wait() from java.lang.Object (final method
 *   used for thread synchronization). Declaring a method also named wait() with a
 *   different return type (WebDriverWait instead of void) causes a compiler error:
 *   "attempting to use incompatible return type". Renaming to getWait() eliminates
 *   the conflict entirely since Object has no getWait() method.
 */
public class SeleniumUtil {

    private static final Logger log = LogManager.getLogger(SeleniumUtil.class);
    private static final int DEFAULT_WAIT = 15;

    // ── PRIVATE INTERNAL HELPERS ───────────────────────────────────────────────

    private static WebDriver driver() {
        return DriverFactory.getDriver();
    }

    private static JavascriptExecutor js() {
        return (JavascriptExecutor) driver();
    }

    /**
     * Returns a WebDriverWait instance with the default timeout.
     *
     * RENAMED from wait() to getWait() to avoid conflict with
     * java.lang.Object.wait() which is inherited by every Java class.
     * Object.wait() is declared as:  public final void wait()
     * Our method returns WebDriverWait — incompatible return type = compile error.
     */
    private static WebDriverWait getWait() {
        return new WebDriverWait(driver(), Duration.ofSeconds(DEFAULT_WAIT));
    }

    /** Returns a WebDriverWait with a custom timeout in seconds. */
    private static WebDriverWait getWait(int timeoutSeconds) {
        return new WebDriverWait(driver(), Duration.ofSeconds(timeoutSeconds));
    }

    private static WebElement waitClickable(By by) {
        return getWait().until(ExpectedConditions.elementToBeClickable(by));
    }

    private static WebElement waitVisible(By by) {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    // ── CLICK HELPERS ──────────────────────────────────────────────────────────

    /**
     * Standard click — waits for element to be clickable before clicking.
     * Use this for all button and link clicks.
     */
    public static void clickElement(By locator) {
        log.info("Click: {}", locator);
        waitClickable(locator).click();
    }

    /**
     * JavaScript click — bypasses overlays and ElementClickInterceptedException.
     * Use as a last resort when regular click is intercepted by another element.
     */
    public static void jsClick(By locator) {
        log.info("JS Click: {}", locator);
        js().executeScript("arguments[0].click();", waitVisible(locator));
    }

    /**
     * Double-click using Selenium Actions class.
     * Use for elements that require a double-click event (e.g. inline edit).
     */
    public static void doubleClick(By locator) {
        log.info("Double-click: {}", locator);
        new Actions(driver()).doubleClick(waitClickable(locator)).perform();
    }

    /**
     * Right-click to open the browser context menu using Actions.
     * Use when testing right-click specific functionality.
     */
    public static void rightClick(By locator) {
        log.info("Right-click: {}", locator);
        new Actions(driver()).contextClick(waitVisible(locator)).perform();
    }

    // ── INPUT HELPERS ──────────────────────────────────────────────────────────

    /**
     * Clears the existing value and types new text into an input field.
     * Waits for the element to be visible before interacting.
     */
    public static void typeText(By locator, String text) {
        log.info("Type '{}' → {}", text, locator);
        WebElement el = waitVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    /**
     * Types text WITHOUT clearing first — appends to existing field value.
     */
    public static void appendText(By locator, String text) {
        log.info("Append '{}' → {}", text, locator);
        waitVisible(locator).sendKeys(text);
    }

    /**
     * Clears a field using Ctrl+A then Delete keyboard shortcut.
     * More reliable than element.clear() for React and Angular controlled inputs
     * that override the default clear behaviour.
     */
    public static void clearField(By locator) {
        log.info("Clear field: {}", locator);
        WebElement el = waitVisible(locator);
        el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        el.sendKeys(Keys.DELETE);
    }

    /**
     * Sends a specific keyboard key to the target element.
     * Example: SeleniumUtil.pressKey(By.id("search"), Keys.ENTER);
     *          SeleniumUtil.pressKey(By.id("field"),  Keys.TAB);
     */
    public static void pressKey(By locator, Keys key) {
        log.info("Press key '{}' on: {}", key, locator);
        waitVisible(locator).sendKeys(key);
    }

    // ── DROPDOWN HELPERS ───────────────────────────────────────────────────────

    /**
     * Selects a standard HTML &lt;select&gt; dropdown option by its visible text.
     * Example: selectByVisibleText(By.id("country"), "India")
     */
    public static void selectByVisibleText(By locator, String text) {
        log.info("Select by visible text '{}' from: {}", text, locator);
        new Select(waitVisible(locator)).selectByVisibleText(text);
    }

    /**
     * Selects a dropdown option by its value attribute.
     * Example: selectByValue(By.id("country"), "IN")
     */
    public static void selectByValue(By locator, String value) {
        log.info("Select by value '{}' from: {}", value, locator);
        new Select(waitVisible(locator)).selectByValue(value);
    }

    /**
     * Selects a dropdown option by its zero-based index position.
     * Example: selectByIndex(By.id("month"), 0) selects the first option.
     */
    public static void selectByIndex(By locator, int index) {
        log.info("Select by index {} from: {}", index, locator);
        new Select(waitVisible(locator)).selectByIndex(index);
    }

    /**
     * Returns the text of the currently selected dropdown option.
     */
    public static String getSelectedOption(By locator) {
        String selected = new Select(waitVisible(locator))
                .getFirstSelectedOption().getText();
        log.info("Selected option: '{}'", selected);
        return selected;
    }

    /**
     * Returns all option texts from a dropdown as a List of Strings.
     * Useful for asserting dropdown contents in tests.
     */
    public static List<String> getAllDropdownOptions(By locator) {
        List<String> texts = new ArrayList<>();
        new Select(waitVisible(locator)).getOptions()
                .forEach(o -> texts.add(o.getText().trim()));
        log.info("Dropdown options: {}", texts);
        return texts;
    }

    // ── FRAME HELPERS ──────────────────────────────────────────────────────────

    /**
     * Switches WebDriver context into an iframe identified by a locator.
     * Waits until the frame is available before switching.
     * Always call switchToDefaultContent() after finishing with iframe.
     */
    public static void switchToFrame(By frameLocator) {
        log.info("Switch to frame: {}", frameLocator);
        getWait().until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
    }

    /**
     * Switches into an iframe identified by its name or id attribute value.
     */
    public static void switchToFrameByNameOrId(String nameOrId) {
        log.info("Switch to frame by name/id: '{}'", nameOrId);
        getWait().until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(nameOrId));
    }

    /**
     * Switches into an iframe by its zero-based index on the page.
     */
    public static void switchToFrameByIndex(int index) {
        log.info("Switch to frame by index: {}", index);
        driver().switchTo().frame(index);
    }

    /**
     * Exits all frames and returns the driver context to the main page.
     * Always call this after finishing interactions inside an iframe.
     */
    public static void switchToDefaultContent() {
        log.info("Switch back to default content (exit all frames)");
        driver().switchTo().defaultContent();
    }

    /**
     * Switches to the parent frame — one level up from a nested frame.
     */
    public static void switchToParentFrame() {
        log.info("Switch to parent frame");
        driver().switchTo().parentFrame();
    }

    // ── ALERT HELPERS ──────────────────────────────────────────────────────────

    /**
     * Waits for a browser alert to appear then accepts it (clicks OK).
     */
    public static void acceptAlert() {
        log.info("Accept alert (click OK)");
        getWait().until(ExpectedConditions.alertIsPresent()).accept();
    }

    /**
     * Waits for a browser alert then dismisses it (clicks Cancel).
     */
    public static void dismissAlert() {
        log.info("Dismiss alert (click Cancel)");
        getWait().until(ExpectedConditions.alertIsPresent()).dismiss();
    }

    /**
     * Returns the text message displayed inside the current browser alert.
     */
    public static String getAlertText() {
        String text = getWait().until(ExpectedConditions.alertIsPresent()).getText();
        log.info("Alert text: '{}'", text);
        return text;
    }

    /**
     * Types text into a prompt alert dialog then accepts it.
     * Use for window.prompt() alerts that require text input.
     */
    public static void typeInAlert(String text) {
        log.info("Type '{}' in alert then accept", text);
        Alert alert = getWait().until(ExpectedConditions.alertIsPresent());
        alert.sendKeys(text);
        alert.accept();
    }

    // ── WINDOW / TAB HELPERS ───────────────────────────────────────────────────

    /**
     * Returns the handle of the currently focused browser window/tab.
     * Capture this before performing an action that opens a new window.
     */
    public static String getCurrentWindowHandle() {
        return driver().getWindowHandle();
    }

    /**
     * Switches driver focus to a newly opened window or tab.
     * Pass the original handle (captured before the new window opened)
     * and this method will switch to whichever handle is not the original.
     *
     * Example:
     *   String original = SeleniumUtil.getCurrentWindowHandle();
     *   // click button that opens new tab
     *   SeleniumUtil.switchToNewWindow(original);
     *   // now interacting in new tab
     */
    public static void switchToNewWindow(String originalHandle) {
        log.info("Switching to new window/tab");
        for (String handle : driver().getWindowHandles()) {
            if (!handle.equals(originalHandle)) {
                driver().switchTo().window(handle);
                log.info("Switched to new window: {}", handle);
                return;
            }
        }
        throw new RuntimeException(
                "No new window found. Only one window handle exists.");
    }

    /**
     * Switches driver focus to a specific window by its handle string.
     */
    public static void switchToWindow(String windowHandle) {
        log.info("Switch to window: {}", windowHandle);
        driver().switchTo().window(windowHandle);
    }

    /**
     * Closes the current window/tab and switches back to the original window.
     */
    public static void closeCurrentWindowAndSwitch(String originalHandle) {
        driver().close();
        driver().switchTo().window(originalHandle);
        log.info("Closed current tab and switched back to original window");
    }

    /**
     * Returns the total count of open browser windows and tabs.
     */
    public static int getWindowCount() {
        return driver().getWindowHandles().size();
    }

    // ── SCROLL HELPERS ─────────────────────────────────────────────────────────

    /**
     * Scrolls the matching element into the centre of the visible viewport.
     * Use before clicking elements that may be below the fold.
     */
    public static void scrollIntoView(By locator) {
        log.info("Scroll into view: {}", locator);
        js().executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});",
                driver().findElement(locator));
    }

    /**
     * Scrolls a WebElement reference into the visible viewport.
     * Use when you already have the WebElement object from a previous findElement.
     */
    public static void scrollIntoView(WebElement element) {
        js().executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});",
                element);
    }

    /**
     * Scrolls the page to the very top (position 0,0).
     */
    public static void scrollToTop() {
        log.info("Scroll to page top");
        js().executeScript("window.scrollTo(0, 0);");
    }

    /**
     * Scrolls the page to the very bottom (full document height).
     */
    public static void scrollToBottom() {
        log.info("Scroll to page bottom");
        js().executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    /**
     * Scrolls down the page by a specific number of pixels from current position.
     * Use a negative value to scroll upward.
     */
    public static void scrollByPixels(int pixels) {
        log.info("Scroll by {} pixels", pixels);
        js().executeScript("window.scrollBy(0, " + pixels + ");");
    }

    // ── DRAG AND DROP ──────────────────────────────────────────────────────────

    /**
     * Drags the source element and drops it onto the target element using Actions.
     * Both elements must be visible and interactable.
     */
    public static void dragAndDrop(By sourceLocator, By targetLocator) {
        log.info("Drag '{}' and drop onto '{}'", sourceLocator, targetLocator);
        new Actions(driver())
                .dragAndDrop(waitVisible(sourceLocator), waitVisible(targetLocator))
                .perform();
    }

    /**
     * Drags an element by a pixel offset (x, y) from its current position.
     * Use when there is no specific drop target element — just a coordinate.
     */
    public static void dragAndDropByOffset(By sourceLocator, int xOffset, int yOffset) {
        log.info("Drag '{}' by offset ({}, {})", sourceLocator, xOffset, yOffset);
        new Actions(driver())
                .dragAndDropBy(waitVisible(sourceLocator), xOffset, yOffset)
                .perform();
    }

    // ── HOVER / MOUSE ─────────────────────────────────────────────────────────

    /**
     * Moves the mouse pointer over an element to trigger CSS :hover effects
     * and mouseover events. Commonly used to reveal hidden sub-menus.
     */
    public static void hoverOverElement(By locator) {
        log.info("Hover over: {}", locator);
        new Actions(driver()).moveToElement(waitVisible(locator)).perform();
    }

    /**
     * Hovers over one element then clicks another in a single Actions chain.
     * Use for navigation menus where hovering reveals a clickable sub-item.
     *
     * Example:
     *   SeleniumUtil.hoverAndClick(By.id("menu-products"), By.id("menu-laptops"));
     */
    public static void hoverAndClick(By hoverLocator, By clickLocator) {
        log.info("Hover '{}' then click '{}'", hoverLocator, clickLocator);
        new Actions(driver())
                .moveToElement(waitVisible(hoverLocator))
                .click(waitClickable(clickLocator))
                .perform();
    }

    // ── STATE VERIFIERS ────────────────────────────────────────────────────────

    /**
     * Returns true if the element is present in DOM AND currently visible on screen.
     * Returns false if the element is absent or has display:none / visibility:hidden.
     */
    public static boolean isDisplayed(By locator) {
        try {
            return driver().findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns true if the element is enabled (not disabled or greyed out).
     * Use to verify form fields and buttons before interacting with them.
     */
    public static boolean isEnabled(By locator) {
        try {
            return driver().findElement(locator).isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns true if a checkbox or radio button is currently checked/selected.
     */
    public static boolean isSelected(By locator) {
        try {
            return driver().findElement(locator).isSelected();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns true if the element exists anywhere in the DOM — even if hidden.
     * Faster than isDisplayed() when you only need to confirm DOM presence.
     */
    public static boolean isPresent(By locator) {
        return !driver().findElements(locator).isEmpty();
    }

    // ── TEXT / ATTRIBUTE GETTERS ───────────────────────────────────────────────

    /**
     * Returns the visible inner text of an element (trimmed of whitespace).
     */
    public static String getText(By locator) {
        return waitVisible(locator).getText().trim();
    }

    /**
     * Returns the value of any HTML attribute on the element.
     * Example: getAttribute(By.id("link"), "href")
     *          getAttribute(By.id("input"), "placeholder")
     *          getAttribute(By.id("btn"),   "class")
     */
    public static String getAttribute(By locator, String attribute) {
        return waitVisible(locator).getAttribute(attribute);
    }

    /**
     * Returns the current value of an input field via the value attribute.
     * Equivalent to getAttribute(locator, "value") — provided as a convenience.
     */
    public static String getInputValue(By locator) {
        return getAttribute(locator, "value");
    }

    /**
     * Returns the title of the current page as a String.
     */
    public static String getPageTitle() {
        return driver().getTitle();
    }

    /**
     * Returns the full URL of the current page.
     */
    public static String getCurrentUrl() {
        return driver().getCurrentUrl();
    }

    /**
     * Returns the visible text of ALL elements matching the locator as a List.
     * Useful for asserting the contents of a table column, list, or set of labels.
     * Example: getAllTexts(By.cssSelector("table tbody tr td:first-child"))
     */
    public static List<String> getAllTexts(By locator) {
        List<String> texts = new ArrayList<>();
        driver().findElements(locator).forEach(e -> texts.add(e.getText().trim()));
        return texts;
    }

    /**
     * Returns the count of all elements on the page matching the given locator.
     */
    public static int getElementCount(By locator) {
        return driver().findElements(locator).size();
    }

    // ── JAVASCRIPT EXECUTOR ────────────────────────────────────────────────────

    /**
     * Executes arbitrary JavaScript in the current browser context.
     * Returns the script's return value (cast to the appropriate type as needed).
     * Example: SeleniumUtil.executeScript("return document.title;")
     */
    public static Object executeScript(String script, Object... args) {
        log.debug("Execute JS: {}", script);
        return js().executeScript(script, args);
    }

    /**
     * Highlights an element with a red border and yellow background.
     * Useful for visual debugging — call just before taking a screenshot.
     */
    public static void highlightElement(By locator) {
        js().executeScript(
                "arguments[0].style.border='3px solid red';" +
                        "arguments[0].style.background='yellow';",
                driver().findElement(locator));
    }

    /**
     * Sets the value of an input field directly via JavaScript.
     * Use for date pickers, readonly fields, and React/Angular controlled inputs
     * where Selenium's sendKeys() is blocked or ignored by the component.
     */
    public static void setValueByJS(By locator, String value) {
        log.info("JS setValueByJS '{}' on: {}", value, locator);
        js().executeScript(
                "arguments[0].value='" + value + "';",
                driver().findElement(locator));
    }

    /**
     * Reads the current value of an input field directly from the DOM via JS.
     * More reliable than getAttribute("value") for dynamically updated inputs.
     */
    public static String getValueByJS(By locator) {
        return (String) js().executeScript(
                "return arguments[0].value;",
                driver().findElement(locator));
    }

    /**
     * Waits until the browser's document.readyState equals 'complete'.
     * Call this after page navigation or form submission to ensure the page
     * has fully loaded before the next interaction.
     */
    public static void waitForPageLoad() {
        log.info("Waiting for page load (document.readyState == complete)");
        getWait().until(d ->
                js().executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Waits until the page is loaded with a custom timeout.
     *
     * @param timeoutSeconds max seconds to wait for page load completion
     */
    public static void waitForPageLoad(int timeoutSeconds) {
        log.info("Waiting for page load — timeout: {}s", timeoutSeconds);
        getWait(timeoutSeconds).until(d ->
                js().executeScript("return document.readyState").equals("complete"));
    }

    // Private constructor — utility class, never instantiated
    private SeleniumUtil() {}
}
