package com.expertrise.training.selenium.browser.webelements;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.time.Duration;

public class FrameHandlingTest  {  // or extend your base class
    public WebDriver driver;
    WebDriverWait wait;
    @BeforeTest
    public void setupBrowserConfiguration() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.tutorialspoint.com/selenium/practice/frames.php");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    @Test(enabled = false)
    public void handleFramesByIndex() {

        System.out.println(">>> Switching to Iframe 1 by Index");
        driver.switchTo().frame(0);
        // Now you can interact with elements INSIDE Iframe 1
        String iframe1Text = driver.findElement(By.tagName("body")).getText();
        System.out.println("Iframe 1 Body Text : " + iframe1Text);
        // switch back to main page after working inside iframe
        driver.switchTo().defaultContent();
        System.out.println("Switched back to Main Page");
        System.out.println("---------------------------------------------------");
    }

    @Test (enabled = false)
    public void handleFramesByNameOrId() {
        System.out.println(">>> Switching to Iframe 2 by Name or ID");
        driver.switchTo().frame("Iframe 2");
        // Now you can interact with elements INSIDE Iframe 2
        String iframe2Text = driver.findElement(By.tagName("body")).getText();
        System.out.println("Iframe 2 Body Text : " + iframe2Text);
        // switch back to main page after working inside iframe
        driver.switchTo().defaultContent();
        System.out.println("Switched back to Main Page");
        System.out.println("---------------------------------------------------");
    }
    @Test (enabled = true)
    public void handleFramesByElement() {
        System.out.println(">>> Switching to Iframe 1 by WebElement");
        WebElement iframe1 = driver.findElement(
                By.xpath("//iframe[@src='new-tab-sample.php' and @height='150px']"));
        // wait for the iframe to be available and switch to it
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                By.xpath("//iframe[@src='new-tab-sample.php' and @height='150px']")));
       // driver.switchTo().frame(iframe1);
        String iframe1Title = driver.getTitle();
        System.out.println("Inside Iframe 1 - Page Title : " + iframe1Title);
        driver.switchTo().defaultContent();  // Back to main page
        System.out.println("---------------------------------------------------");
    }



    @AfterTest
    public void tearDownBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}


