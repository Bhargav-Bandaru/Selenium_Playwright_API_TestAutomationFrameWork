package com.expertrise.training.selenium.browser.webelements;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class AutoSuggestionTest {
    // extend your base class
    public WebDriver driver;

    @BeforeTest
    public void setupBrowserConfiguration() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.google.com");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    @Test(enabled = false)
    public void handleAutoSuggestions() throws InterruptedException {
        // ───  Find the search box and type "Selenium" ─────────────────
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement searchBox = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.name("q")));
        searchBox.clear();
        searchBox.sendKeys("Selenium");
        System.out.println("Typed 'Selenium' in the search box");

        // ───  Wait for autocomplete suggestions to appear ─────────────
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//ul[@role='listbox']//li")));

        // Small wait to let all suggestions load fully
        Thread.sleep(1000);

        // ─── Fetch ALL suggestions and print them ────────────────────
        List<WebElement> allSuggestions = driver.findElements(
                By.xpath("//ul[@role='listbox']//li//span[1]"));
        System.out.println("---------------------------------------------------");
        System.out.println("Total suggestions found: " + allSuggestions.size());
        System.out.println("---------------------------------------------------");

        // Store all suggestion texts
        boolean isIdePresent = false;
        String targetText = "selenium ide";
        int ideIndex = -1;
        for (int i = 0; i < allSuggestions.size(); i++) {
            String suggestionText = allSuggestions.get(i).getText().trim().toLowerCase();
            System.out.println("Suggestion " + (i + 1) + ": " + suggestionText);
            // ─── Check if "selenium ide" exists in suggestions ────────
            if (suggestionText.contains("ide")) {
                isIdePresent = true;
                ideIndex = i;
            }
        }
        System.out.println("---------------------------------------------------");
        // ───  Assert whether "selenium ide" is present ────────────────
        System.out.println("Is 'selenium ide' present in suggestions? " + isIdePresent);
        Assert.assertTrue(isIdePresent,
                "FAIL: 'selenium ide' was NOT found in the autocomplete suggestions!");
        System.out.println("PASS: 'selenium ide' found at position: " + (ideIndex + 1));

        // ───  Re-fetch suggestions and click on "selenium ide" ─────────
        // Re-fetching because StaleElementReferenceException can occur
        List<WebElement> freshSuggestions = driver.findElements(
                By.xpath("//ul[@role='listbox']//li//span[1]"));

        for (WebElement suggestion : freshSuggestions) {
            String text = suggestion.getText().trim().toLowerCase();
            if (text.contains("ide") && !text.contains("extension") && !text.contains("interview")) {
                System.out.println("Clicking on suggestion: " + suggestion.getText());
                suggestion.click();
                break;
            }
        }

        // ─── Verify the page navigated correctly ─────────────────────
        wait.until(ExpectedConditions.or(
                ExpectedConditions.titleContains("ide"),
                ExpectedConditions.titleContains("IDE")
        ));

        System.out.println("Page Title after click : " + driver.getTitle());
        System.out.println("Page URL after click   : " + driver.getCurrentUrl());
        Assert.assertTrue(driver.getTitle().toLowerCase().contains("ide"),
                "FAIL: Page title does not contain 'ide' after clicking suggestion!");
        System.out.println("PASS: Successfully navigated after clicking 'selenium ide'");
    }

    // ─── Select suggestion by ARROW KEYS (Keyboard Navigation) ────────
    @Test(enabled = true)
    public void handleAutoSuggestionsByKeyboard() throws InterruptedException {
        driver.get("https://www.google.com");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement searchBox = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("q")));
        searchBox.clear();
        searchBox.sendKeys("Selenium");
        // Wait for suggestions to load
        Thread.sleep(1500);
        // Navigate using arrow keys to select suggestion
        // Arrow DOWN moves to next suggestion
        // Arrow UP moves to previous suggestion
        // ENTER selects the highlighted suggestion
        System.out.println("Navigating suggestions using keyboard...");
        // Press DOWN arrow 4 times to reach "selenium ide" (4th suggestion)
        for (int i = 0; i < 4; i++) {
            searchBox.sendKeys(Keys.ARROW_DOWN);
            Thread.sleep(300); // Small pause between key presses
            System.out.println("Pressed DOWN arrow: " + (i + 1) + " time(s)");
        }
        // Get the currently highlighted suggestion text
        WebElement highlighted = driver.findElement(
                By.xpath("//ul[@role='listbox']//li[@aria-selected='true']//span[1]"));
        System.out.println("Highlighted suggestion: " + highlighted.getText());

        // Press ENTER to select
        searchBox.sendKeys(Keys.ENTER);
        System.out.println("Pressed ENTER to select suggestion");
        System.out.println("Page Title: " + driver.getTitle());
        System.out.println("Page URL  : " + driver.getCurrentUrl());
    }
}
