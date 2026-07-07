package com.expertrise.training.selenium.actions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

public class SliderTest  {  // extend your base class

    public WebDriver driver;

    @BeforeTest
    public void setupBrowserConfiguration() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://testautomationpractice.blogspot.com/");
    }

    @Test
    public void handleDualRangeSlider() {


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // ─── STEP 1: Locate the slider container and its two handles ──────────
        WebElement sliderContainer = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("slider-range")));

        List<WebElement> handles = sliderContainer.findElements(
                By.className("ui-slider-handle"));

        WebElement minHandle = handles.get(0); // left handle  (style: left: 15%)
        WebElement maxHandle = handles.get(1); // right handle (style: left: 60%)

        System.out.println("Total handles found: " + handles.size());
        System.out.println("Min handle initial position: " + minHandle.getAttribute("style"));
        System.out.println("Max handle initial position: " + maxHandle.getAttribute("style"));

        // ─── STEP 2: Read the CURRENT price range text before moving slider ───
        WebElement priceRangeText = driver.findElement(By.id("amount")); // adjust locator if different
        System.out.println("Initial Price Range: " + priceRangeText.getText());

        // ─── STEP 3: Move the slider using ARROW KEYS (most reliable method) ──
        moveSliderByArrowKeys(minHandle, Keys.ARROW_RIGHT, 5);  // increase min by 5 steps
        moveSliderByArrowKeys(maxHandle, Keys.ARROW_LEFT, 10);  // decrease max by 10 steps

        // ─── STEP 4: Verify the updated price range ────────────────────────────
        String updatedRange = priceRangeText.getText();
        System.out.println("Updated Price Range: " + updatedRange);

        Assert.assertNotEquals(updatedRange, "$75 - $300",
                "FAIL: Slider value did not change after moving handles!");
        System.out.println("PASS: Slider successfully moved");
    }

    // ─── HELPER 1: Move slider handle using keyboard arrow keys ───────────────
    // jQuery UI sliders respond to arrow keys when the handle has focus —
    // this is the MOST RELIABLE method (each press = exactly 1 step)
    private void moveSliderByArrowKeys(WebElement handle, Keys direction, int times) {
        handle.click(); // focus the handle first
        for (int i = 0; i < times; i++) {
            handle.sendKeys(direction);
        }
        System.out.println("Moved handle " + times + " times using " + direction);
    }

    // ─── HELPER 2: Move slider using Actions class (drag and drop by pixels) ──
    @Test
    public void moveSliderUsingDragAndDrop() {


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement sliderContainer = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("slider-range")));
        List<WebElement> handles = sliderContainer.findElements(By.className("ui-slider-handle"));

        WebElement minHandle = handles.get(0);
        WebElement maxHandle = handles.get(1);

        Actions actions = new Actions(driver);

        // Drag MIN handle 50 pixels to the RIGHT (increase minimum price)
        actions.clickAndHold(minHandle)
                .moveByOffset(50, 0)
                .release()
                .build()
                .perform();
        System.out.println("Dragged min handle +50px to the right");

        // Drag MAX handle 80 pixels to the LEFT (decrease maximum price)
        actions.clickAndHold(maxHandle)
                .moveByOffset(-80, 0)
                .release()
                .build()
                .perform();
        System.out.println("Dragged max handle -80px to the left");

        WebElement priceRangeText = driver.findElement(By.id("amount"));
        System.out.println("Updated Price Range: " + priceRangeText.getText());
    }

    // ─── HELPER 3: Move slider to a TARGET percentage using JavaScript ────────
    // Most precise method - calculates exact pixel position based on slider width
    @Test
    public void moveSliderToExactPercentage() {

        driver.get("https://www.testautomationpractice.blogspot.com");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement sliderContainer = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("slider-range")));
        List<WebElement> handles = sliderContainer.findElements(By.className("ui-slider-handle"));

        WebElement minHandle = handles.get(0);
        int sliderWidth = sliderContainer.getSize().getWidth();

        // Move min handle to 30% position
        int targetXOffset = (int) (sliderWidth * 0.30) - (sliderWidth * 15 / 100); // current is 15%

        Actions actions = new Actions(driver);
        actions.clickAndHold(minHandle)
                .moveByOffset(targetXOffset, 0)
                .release()
                .build()
                .perform();

        System.out.println("Moved min handle to ~30% of slider width");
        System.out.println("Final Price Range: " + driver.findElement(By.id("range")).getText());
    }

    // ─── HELPER 4: Read exact percentage from style attribute ─────────────────
    @Test
    public void readSliderHandlePositions() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement sliderContainer = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("slider-range")));
        List<WebElement> handles = sliderContainer.findElements(By.className("ui-slider-handle"));

        for (int i = 0; i < handles.size(); i++) {
            String style = handles.get(i).getAttribute("style"); // e.g. "left: 15%;"
            String percentage = style.replaceAll("[^0-9.]", "");  // extract just the number
            System.out.println("Handle " + (i + 1) + " position: " + percentage + "%");
        }
    }
    @AfterTest
    public void tearDown() throws Exception {
        driver.quit();
    }
}