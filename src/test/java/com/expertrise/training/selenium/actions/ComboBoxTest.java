package com.expertrise.training.selenium.actions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ComboBoxTest {


    public WebDriver driver;

    @BeforeTest
    public void setUp() throws Exception {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://testautomationpractice.blogspot.com/");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    }

    @Test
    public void comboBoxTestTest() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // ─── STEP 1: Click the combo box input to open the dropdown ───────────
        WebElement comboBox = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("comboBox")));
        comboBox.click();
        System.out.println("Clicked on comboBox input");

        // ─── STEP 2: Type to filter the list (e.g., "item") ────────────────────
        comboBox.sendKeys("item");
        System.out.println("Typed 'item' to filter the dropdown");
        Thread.sleep(500); // small wait for filtering to apply

        // ─── STEP 3: Wait for dropdown options to appear ───────────────────────
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@id='dropdown']//div[@class='option']")));

        // ─── STEP 4: Fetch ALL visible options in the dropdown ─────────────────
        List<WebElement> allOptions = driver.findElements(
                By.xpath("//div[@id='dropdown']//div[@class='option']"));

        System.out.println("---------------------------------------------------");
        System.out.println("Total options found: " + allOptions.size());
        System.out.println("---------------------------------------------------");

        for (int i = 0; i < allOptions.size(); i++) {
            System.out.println("Option " + (i + 1) + ": " + allOptions.get(i).getText());
        }
        System.out.println("---------------------------------------------------");

        // ─── STEP 5: Scroll inside the dropdown to find a SPECIFIC item ────────
        // Since dropdown has its OWN internal scrollbar (not page scroll),
        // we must scroll within the dropdown div, not the page.
        String targetItem = "Item 100";
        WebElement targetOption = scrollToOptionInDropdown(targetItem);

        // ─── STEP 6: Verify item exists and select it ──────────────────────────
        Assert.assertNotNull(targetOption, "FAIL: '" + targetItem + "' not found in dropdown!");
        System.out.println("PASS: Found option -> " + targetOption.getText());

        targetOption.click();
        System.out.println("Clicked on: " + targetItem);
        // ─── STEP 7: Verify selection reflected in the input box ───────────────
        String selectedValue = comboBox.getAttribute("value");
        System.out.println("ComboBox value after selection: " + selectedValue);
        Assert.assertEquals(selectedValue, targetItem,
                "FAIL: ComboBox value does not match selected item!");
        System.out.println("PASS: '" + targetItem + "' successfully selected");
    }


   @Test(enabled = false)
    public void selectdropDownOptionsDirectily(String targetItem) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement comboBox = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("comboBox")));
        comboBox.click();
        // Directly use JS to locate and click without typing filter text
        WebElement targetOption = scrollToOptionInDropdown("Item 50");

        if (targetOption != null) {
            targetOption.click();
            System.out.println("Selected: Item 50");
        } else {
            System.out.println("Item 50 not found!");
        }
    }

    // ─── HELPER: Scroll within the dropdown's internal scroll container ───────
    private WebElement scrollToOptionInDropdown(String itemText) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement dropdown = driver.findElement(By.id("dropdown"));
        int maxScrollAttempts = 30;
        for (int attempt = 0; attempt < maxScrollAttempts; attempt++) {
            List<WebElement> options = dropdown.findElements(By.className("option"));
            for (WebElement option : options) {
                if (option.getText().trim().equalsIgnoreCase(itemText)) {
                    // Scroll this specific option into view WITHIN the dropdown container
                    js.executeScript(
                            "arguments[0].scrollIntoView({block: 'center'});", option);
                    return option;
                }
            }

            // Scroll DOWN inside the dropdown div by its scrollHeight, not the page
            js.executeScript("arguments[0].scrollTop += 100;", dropdown);
        }
        return null; // Not found after max attempts
    }

    @AfterTest
    public void tearDown() throws Exception {
        driver.quit();
    }

}
