package com.expertrise.training.selenium.browserInteractions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class ChromeBrowerProfileConfigurationTest {

public WebDriver driver;
   @BeforeTest
   @Parameters({"headless"})
   public void setupBrowserConfiguration(@Optional("false") String headless)
    {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // ─── 1. HEADLESS MODE ────────────────────────────────────────────────
        //Runs browser without UI — used in CI/CD pipelines (Jenkins, GitHub Actions) where no display is available
        if (Boolean.parseBoolean(headless)) {
            options.addArguments("--headless=new");   // New headless (Chrome 112+)
            options.addArguments("--window-size=1920,1080");//Headless has no default size — must set manually to avoid element-not-visible errors
            options.addArguments("--disable-gpu");    // Required for headless on Windows
        }

        // ─── 2. WINDOW & DISPLAY ─────────────────────────────────────────────
        options.addArguments("--start-maximized");//Ensures full screen so all elements are visible and clickable
        options.addArguments("--disable-infobars");  //Removes "Chrome is being controlled by automated software" bar that can block UI elements
        options.addArguments("--disable-notifications"); // Blocks browser notification popups that interrupt test flow
        options.addArguments("--disable-popup-blocking"); //Allows test to handle popups when explicitly needed

        // ─── 3. PERFORMANCE & STABILITY ──────────────────────────────────────
        options.addArguments("--no-sandbox");                    // Required in CI/CD environments
        options.addArguments("--disable-dev-shm-usage");        // Overcomes limited resource problems in Docker
        options.addArguments("--disable-extensions"); //Avoids extensions interfering with test execution
        options.addArguments("--disable-default-apps");
        options.addArguments("--remote-allow-origins=*");       // Fixes WebSocket issues in newer Chrome

        // ─── 4. PAGE LOAD STRATEGY ───────────────────────────────────────────
        // NORMAL   = waits for full page load (default)
        // EAGER    = waits for DOM ready only (faster)
        // NONE     = doesn't wait at all (fastest)
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        // ─── 5. DOWNLOAD SETTINGS ────────────────────────────────────────────
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", System.getProperty("user.dir") + "\\downloads"); //Sets custom download path so tests can verify downloaded files
        prefs.put("download.prompt_for_download", false);       // Auto-download without popup
        prefs.put("download.directory_upgrade", true);
        prefs.put("safebrowsing.enabled", true); //Allows downloads without Chrome flagging them as unsafe
        prefs.put("plugins.always_open_pdf_externally", true);  // Download PDF instead of opening in browser

        // ─── 6. DISABLE PASSWORD SAVE & AUTOFILL ─────────────────────────────
        prefs.put("credentials_enable_service", false); //Prevents "Save Password?" popup from appearing during login tests
        prefs.put("profile.password_manager_enabled", false); //Disables password manager completely
        prefs.put("autofill.profile_enabled", false);//Stops autofill from pre-filling form fields which can cause wrong test data
        options.setExperimentalOption("prefs", prefs); //

        // ─── 7. CHROME PROFILE (Reuse existing logged-in session) ────────────
        // Uncomment to use your existing Chrome profile (keeps cookies/sessions)
        // options.addArguments("--user-data-dir=C:\\Users\\admin\\AppData\\Local\\Google\\Chrome\\User Data");
        // options.addArguments("--profile-directory=Default");  // or "Profile 1", "Profile 2"

        // ─── 8. INCOGNITO MODE ───────────────────────────────────────────────
        // options.addArguments("--incognito");

        // ─── 9. IGNORE SSL / CERTIFICATE ERRORS ──────────────────────────────
        options.addArguments("--ignore-certificate-errors");//Allows testing on staging/dev environments with self-signed certificates
        options.addArguments("--allow-insecure-localhost"); //Same purpose — Selenium-level SSL bypass for HTTP/HTTPS mixed content
        options.setAcceptInsecureCerts(true);//Specifically bypasses SSL errors on localhost test environments

        // ─── 10. PROXY SETTINGS (Corporate Network) ──────────────────────────
        // Proxy proxy = new Proxy();
        // proxy.setHttpProxy("proxy.company.com:8080");
        // proxy.setSslProxy("proxy.company.com:8080");
        // options.setProxy(proxy);

        // ─── 11. LOGGING & DEBUGGING ─────────────────────────────────────────
        options.addArguments("--log-level=3");                  // Suppress console warnings (0=ALL, 3=FATAL)
        options.setExperimentalOption("excludeSwitches",
                java.util.Arrays.asList("enable-logging", "enable-automation"));

        // ─── 12. MOBILE EMULATION (Responsive Testing) ───────────────────────
        // Map<String, String> mobileEmulation = new HashMap<>();
        // mobileEmulation.put("deviceName", "iPhone 14 Pro");   // or "Pixel 7", "Galaxy S20"
        // options.setExperimentalOption("mobileEmulation", mobileEmulation);

        // ─── 13. LANGUAGE ────────────────────────────────────────────────────
        options.addArguments("--lang=en-US");

        // ─── 14. INITIALIZE DRIVER ───────────────────────────────────────────
        driver = new ChromeDriver(options);

        // ─── 15. TIMEOUTS ────────────────────────────────────────────────────
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(15));

        if (!Boolean.parseBoolean(headless)) {
            driver.manage().window().maximize();
        }
    }
    @Test
    public void validateLaunchChromeBrowser()
    {
        driver.get("https://www.google.com");
        System.out.println("Title : " + driver.getTitle());
        System.out.println("URL   : " + driver.getCurrentUrl());
    }

    @AfterTest
    public void tearDownBrowser()
    {
        //driver.close();
        driver.quit();
    }

}
