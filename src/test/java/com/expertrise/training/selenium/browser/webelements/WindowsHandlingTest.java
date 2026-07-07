package com.expertrise.training.selenium.browser.webelements;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Set;

public class WindowsHandlingTest {

    public WebDriver driver;
    @BeforeTest
    public void setupBrowserConfiguration() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://testautomationpractice.blogspot.com/");
    }
    @Test
    public void validateMultipleWindows_Tabs_HandlingTest() {
        driver.findElement(By.id("PopUp")).click();
        String parentWindowHandle = driver.getWindowHandle();
        System.out.println("Parent Window Handle: " + parentWindowHandle);
        System.out.println("Parent Window Title  : " + driver.getTitle());
        System.out.println("Parent Window URL    : " + driver.getCurrentUrl());
        System.out.println("---------------------------------------------------");
        // Get all window handles
        Set<String> allWindowHandles = driver.getWindowHandles();
        // Switch to the new window or tab
        for (String windowHandle : allWindowHandles) {
            if (!windowHandle.equals(parentWindowHandle)) {
                System.out.println("--------------child window details  -------------------------------------");
                driver.switchTo().window(windowHandle);
                System.out.println("Switched to Child Window Handle: " + windowHandle);
                System.out.println("Child Window Title  : " + driver.getTitle());
                System.out.println("Child Window URL    : " + driver.getCurrentUrl());
                System.out.println("---------------------------------------------------");
                System.out.println("Switched to new window/tab with handle: " + windowHandle);
                // Perform actions in the new window/tab
                System.out.println("Title of the new window/tab: " + driver.getTitle());
                if(driver.getTitle().contains("Selenium")||driver.getTitle().contains("Fast and reliable end-to-end testing for modern web apps | Playwright"))
                    System.out.println("validated the Page Title " + driver.getTitle());
                    driver.close(); // Close the new window/tab
                //break;
            }
        }
        // Switch back to the parent window
        driver.switchTo().window(parentWindowHandle);
        System.out.println("Switched back to parent window with handle: " + parentWindowHandle);
        // Validation - confirm only 1 window remains
        System.out.println("Windows remaining after closing child: " + driver.getWindowHandles().size());
    }


    @AfterTest
    public void tearDownBrowser() {
        if (driver != null) {
            driver.quit();
        }
        //  driver.close();

    }





}
