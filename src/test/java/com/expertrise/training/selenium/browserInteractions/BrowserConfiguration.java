package com.expertrise.training.selenium.browserInteractions;

import com.expertrise.automation.config.ConfigManager;
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



public class BrowserConfiguration {

    private static final Logger log = LogManager.getLogger(BrowserConfiguration.class);
    private static final int DEFAULT_TIMEOUT_SECONDS = 30;
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    public void setBrowserType(String browserName) {

        /**
         * Initialises a Selenium WebDriver for the given browser.
         * Reads browser from system property first: -Dbrowser=chrome
         *
         * @param browserName chrome | firefox | edge | safari (case-insensitive)
         */

        //  String browser = System.getProperty("browser", browserName).toLowerCase().trim();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        log.info("Initialising Selenium WebDriver — browser={}, headless={}", browserName, headless);

        WebDriver driver;
        switch (browserName) {
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
        log.info("Selenium WebDriver started — thread={}", Thread.currentThread().getId());
    }

    public void launchBrowser(String applicationUrl) {

        WebDriver driver = null;
        driver.get(applicationUrl);
    }

    /**
     * Returns the WebDriver for the current thread.
     *
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
}

