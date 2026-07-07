package com.expertrise.training.Junit;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.expertrise.automation.config.DriverFactory;
import com.expertrise.automation.config.ConfigManager;

class AutomationPracticeJUnit5Test {

    private WebDriver driver;

    // Runs before each test
    @BeforeEach
    void setUp() {
        try {
            driver = DriverFactory.getDriver();
        } catch (IllegalStateException e) {
            DriverFactory.initSeleniumDriver(ConfigManager.getBrowser());
            driver = DriverFactory.getDriver();
        }
    }

    // Runs after each test
    @AfterEach
    void tearDown() {
        // Use DriverFactory.quitDriver() to ensure ThreadLocal is cleaned up properly
        try {
            DriverFactory.quitDriver();
        } finally {
            driver = null;
        }
    }

    @Test
    @DisplayName("User is on the automation practice page")
    void userIsOnAutomationPracticePage() {
        driver.get("https://testautomationpractice.blogspot.com/");
        Assertions.assertEquals("Automation Testing Practice", driver.getTitle());
    }

    @Test
    @DisplayName("User enters details in the form-group")
    void userEntersDetailsInFormGroup() {
        driver.get("https://testautomationpractice.blogspot.com/");
        driver.findElement(By.id("name")).sendKeys("John Doe");
        driver.findElement(By.id("email")).sendKeys("john@test.com");
        driver.findElement(By.id("phone")).sendKeys("+91 9876543210");
        driver.findElement(By.id("textarea")).sendKeys("India");

        // Example assertion to validate input
        Assertions.assertEquals("John Doe", driver.findElement(By.id("name")).getAttribute("value"));
    }
}
