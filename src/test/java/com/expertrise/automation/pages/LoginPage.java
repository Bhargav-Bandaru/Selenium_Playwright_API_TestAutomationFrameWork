package com.expertrise.automation.pages;

import com.expertrise.automation.config.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * LoginPage — Page Object for https://automationexercise.com/login
 *
 * <p>Locators are extracted from the provided DOM (AutomationExercise.html).
 * All elements use {@code data-qa} attributes — the most stable locator strategy,
 * immune to CSS class or position changes.</p>
 *
 * <p>This class covers:
 * <ul>
 *   <li>Login form  (action=/login)</li>
 *   <li>Signup form (action=/signup)</li>
 *   <li>Subscription form (footer)</li>
 * </ul>
 */
public class LoginPage {

    private static final Logger log = LogManager.getLogger(LoginPage.class);
    private static final int WAIT_SECONDS = 15;

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ── LOGIN FORM (data-qa from DOM) ─────────────────────────────────────────
    @FindBy(css = "[data-qa='login-email']")
    private WebElement loginEmailInput;

    @FindBy(css = "[data-qa='login-password']")
    private WebElement loginPasswordInput;

    @FindBy(css = "[data-qa='login-button']")
    private WebElement loginButton;

    // ── SIGNUP FORM (data-qa from DOM) ────────────────────────────────────────
    @FindBy(css = "[data-qa='signup-name']")
    private WebElement signupNameInput;

    @FindBy(css = "[data-qa='signup-email']")
    private WebElement signupEmailInput;

    @FindBy(css = "[data-qa='signup-button']")
    private WebElement signupButton;

    // ── SUBSCRIPTION (id from DOM) ────────────────────────────────────────────
    @FindBy(id = "susbscribe_email")
    private WebElement subscribeEmailInput;

    @FindBy(id = "subscribe")
    private WebElement subscribeButton;

    // ── NAVIGATION ────────────────────────────────────────────────────────────
    @FindBy(css = "a[href='/login']")
    private WebElement loginNavLink;

    @FindBy(css = "a[href='/products']")
    private WebElement productsNavLink;

    @FindBy(css = "a[href='/view_cart']")
    private WebElement cartNavLink;

    // ── POST-LOGIN verifications ──────────────────────────────────────────────
    // "Logged in as <username>" text appears in nav after successful login
    @FindBy(css = "li a[href='/logout']")
    private WebElement logoutLink;

    @FindBy(xpath = "//a[contains(.,'Logged in as')]")
    private WebElement loggedInBadge;

    @FindBy(css = ".login-form p.text-danger, p[style*='color: red']")
    private WebElement loginErrorMessage;

    @FindBy(css = ".signup-form p.text-danger")
    private WebElement signupErrorMessage;

    // ──────────────────────────────────────────────────────────────────────────
    // CONSTRUCTOR
    // ──────────────────────────────────────────────────────────────────────────

    public LoginPage() {
        this.driver = DriverFactory.getDriver();
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(WAIT_SECONDS));
        PageFactory.initElements(driver, this);
        log.info("LoginPage initialised");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // PAGE ACTIONS
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Navigates to the login page if not already there.
     */
    public LoginPage navigateToLoginPage() {
        if (!driver.getCurrentUrl().contains("/login")) {
            log.info("Navigating to login page");
            wait.until(ExpectedConditions.elementToBeClickable(loginNavLink)).click();
        }
        wait.until(ExpectedConditions.visibilityOf(loginEmailInput));
        log.info("Login page loaded");
        return this;
    }

    /**
     * Enters email in the login email field.
     * @param email valid email address
     */
    public LoginPage enterLoginEmail(String email) {
        log.info("Entering login email: {}", email);
        WebElement el = wait.until(ExpectedConditions.visibilityOf(loginEmailInput));
        el.clear();
        el.sendKeys(email);
        return this;
    }

    /**
     * Enters password in the login password field.
     * @param password user password
     */
    public LoginPage enterLoginPassword(String password) {
        log.info("Entering login password");
        WebElement el = wait.until(ExpectedConditions.visibilityOf(loginPasswordInput));
        el.clear();
        el.sendKeys(password);
        return this;
    }

    /**
     * Clicks the Login button and waits for navigation.
     */
    public LoginPage clickLoginButton() {
        log.info("Clicking Login button");
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        return this;
    }

    /**
     * Enters name in the signup name field.
     */
    public LoginPage enterSignupName(String name) {
        log.info("Entering signup name: {}", name);
        WebElement el = wait.until(ExpectedConditions.visibilityOf(signupNameInput));
        el.clear();
        el.sendKeys(name);
        return this;
    }

    /**
     * Enters email in the signup email field.
     */
    public LoginPage enterSignupEmail(String email) {
        log.info("Entering signup email: {}", email);
        WebElement el = wait.until(ExpectedConditions.visibilityOf(signupEmailInput));
        el.clear();
        el.sendKeys(email);
        return this;
    }

    /**
     * Clicks the Signup button.
     */
    public LoginPage clickSignupButton() {
        log.info("Clicking Signup button");
        wait.until(ExpectedConditions.elementToBeClickable(signupButton)).click();
        return this;
    }

    /**
     * Subscribes to newsletter with given email.
     */
    public LoginPage subscribeWithEmail(String email) {
        log.info("Subscribing with email: {}", email);
        WebElement el = wait.until(ExpectedConditions.visibilityOf(subscribeEmailInput));
        el.clear();
        el.sendKeys(email);
        wait.until(ExpectedConditions.elementToBeClickable(subscribeButton)).click();
        return this;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // STATE / VERIFICATION METHODS
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Returns true if the "Logged in as" badge is visible in the navigation.
     */
    public boolean isLoggedIn() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(loggedInBadge)).isDisplayed();
        } catch (Exception e) {
            log.debug("loggedInBadge not found — user not logged in");
            return false;
        }
    }

    /**
     * Returns the logged-in username displayed in the nav badge.
     */
    public String getLoggedInUsername() {
        try {
            String text = loggedInBadge.getText();   // "Logged in as Bhargav"
            return text.replace("Logged in as", "").trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Returns true if a login error message is shown (wrong credentials).
     */
    public boolean isLoginErrorDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(loginErrorMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the text of the login error message.
     */
    public String getLoginErrorText() {
        try { return loginErrorMessage.getText().trim(); }
        catch (Exception e) { return ""; }
    }

    /**
     * Returns true if a signup error message is displayed (email already registered).
     */
    public boolean isSignupErrorDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(signupErrorMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if the logout link is present (user is logged in).
     */
    public boolean isLogoutLinkPresent() {
        try {
            return logoutLink.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Clicks Logout.
     */
    public void clickLogout() {
        log.info("Clicking Logout");
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
    }

    /**
     * Returns the current page title.
     */
    public String getPageTitle() { return driver.getTitle(); }

    /**
     * Returns the current URL.
     */
    public String getCurrentUrl() { return driver.getCurrentUrl(); }
}
