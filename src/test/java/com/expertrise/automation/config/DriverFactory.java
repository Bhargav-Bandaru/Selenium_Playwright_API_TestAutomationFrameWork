package com.expertrise.automation.config;

import com.microsoft.playwright.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;

/**
 * DriverFactory — manages WebDriver (Selenium) and Playwright browser instances.
 *
 * <p>Uses ThreadLocal so every parallel test thread gets its own isolated browser,
 * preventing race conditions and session conflicts during parallel execution.</p>
 *
 * <p>Usage:
 * <pre>
 *   DriverFactory.initSeleniumDriver("chrome");       // start Selenium Chrome
 *   WebDriver driver = DriverFactory.getDriver();     // get current thread driver
 *   DriverFactory.quitDriver();                       // quit + clean up
 *
 *   DriverFactory.initPlaywrightDriver("chromium");   // start Playwright
 *   Page page = DriverFactory.getPlaywrightPage();
 *   DriverFactory.quitPlaywright();
 * </pre>
 */
public class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);

    /* ── Selenium ── */
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    /* ── Playwright ── */
    private static final ThreadLocal<Playwright>    playwrightThread = new ThreadLocal<>();
    private static final ThreadLocal<Browser>       browserThread    = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext>contextThread    = new ThreadLocal<>();
    private static final ThreadLocal<Page>          pageThread       = new ThreadLocal<>();

    private static final int DEFAULT_TIMEOUT_SECONDS = 30;

    // ──────────────────────────────────────────────────────────────────────────
    // SELENIUM — init / get / quit
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Initialises a Selenium WebDriver for the given browser.
     * Reads browser from system property first: -Dbrowser=chrome
     *
     * @param browserName chrome | firefox | edge | safari (case-insensitive)
     */
    public static void initSeleniumDriver(String browserName) {
        String browser = System.getProperty("browser", browserName).toLowerCase().trim();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        log.info("Initialising Selenium WebDriver — browser={}, headless={}", browser, headless);

        WebDriver driver;
        switch (browser) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();
                if (headless) options.addArguments("-headless");
                driver = new FirefoxDriver(options);
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
            }
            case "safari" -> driver = new SafariDriver();
            default -> {                                // chrome (default)
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                if (headless) options.addArguments("--headless=new", "--no-sandbox",
                        "--disable-dev-shm-usage", "--window-size=1920,1080");
                options.addArguments("--start-maximized", "--disable-notifications",
                        "--disable-popup-blocking");
                driver = new ChromeDriver(options);
            }
        }

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0)); // use explicit waits only
        driver.manage().window().maximize();

        driverThread.set(driver);
        log.info("Selenium WebDriver started — thread={}", Thread.currentThread().getId());
    }

    /**
     * Returns the WebDriver for the current thread.
     * @throws IllegalStateException if driver not initialised
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThread.get();
        if (driver == null) {
            throw new IllegalStateException(
                "WebDriver not initialised for thread " + Thread.currentThread().getId() +
                ". Call DriverFactory.initSeleniumDriver() in @BeforeMethod.");
        }
        return driver;
    }

    /**
     * Quits the Selenium WebDriver and removes it from ThreadLocal to prevent memory leaks.
     */
    public static void quitDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            try {
                driver.quit();
                log.info("Selenium WebDriver quit — thread={}", Thread.currentThread().getId());
            } catch (Exception e) {
                log.warn("Error quitting WebDriver: {}", e.getMessage());
            } finally {
                driverThread.remove();
            }
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // PLAYWRIGHT — init / get / quit
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Initialises a Playwright browser and Page for the current thread.
     *
     * @param browserName chromium | firefox | webkit (case-insensitive)
     */
    public static void initPlaywrightDriver(String browserName) {
        String browser = System.getProperty("pw.browser", browserName).toLowerCase().trim();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        log.info("Initialising Playwright — browser={}, headless={}", browser, headless);

        Playwright playwright = Playwright.create();
        Browser pwBrowser;

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setTimeout(60_000);

        pwBrowser = switch (browser) {
            case "firefox" -> playwright.firefox().launch(launchOptions);
            case "webkit"  -> playwright.webkit().launch(launchOptions);
            default        -> playwright.chromium().launch(launchOptions);  // chromium default
        };

        BrowserContext context = pwBrowser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
                .setIgnoreHTTPSErrors(true));
        context.setDefaultTimeout(DEFAULT_TIMEOUT_SECONDS * 1000L);

        Page page = context.newPage();

        playwrightThread.set(playwright);
        browserThread.set(pwBrowser);
        contextThread.set(context);
        pageThread.set(page);

        log.info("Playwright Page created — thread={}", Thread.currentThread().getId());
    }

    /** Returns the Playwright Page for the current thread. */
    public static Page getPlaywrightPage() {
        Page page = pageThread.get();
        if (page == null) throw new IllegalStateException(
            "Playwright Page not initialised. Call DriverFactory.initPlaywrightDriver().");
        return page;
    }

    /** Returns the Playwright BrowserContext for the current thread. */
    public static BrowserContext getPlaywrightContext() { return contextThread.get(); }

    /**
     * Closes all Playwright resources for the current thread (page → context → browser → playwright).
     */
    public static void quitPlaywright() {
        try {
            if (pageThread.get()    != null) { pageThread.get().close();    pageThread.remove(); }
            if (contextThread.get() != null) { contextThread.get().close(); contextThread.remove(); }
            if (browserThread.get() != null) { browserThread.get().close(); browserThread.remove(); }
            if (playwrightThread.get() != null) { playwrightThread.get().close(); playwrightThread.remove(); }
            log.info("Playwright resources closed — thread={}", Thread.currentThread().getId());
        } catch (Exception e) {
            log.warn("Error closing Playwright: {}", e.getMessage());
        }
    }

    private DriverFactory() { /* utility class */ }
}
