package com.expertrise.training.selenium.browserInteractions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class LaunchFireFoxBrowserTest {

    public WebDriver driver;
    @BeforeTest
    public void setupBrowserConfiguration()
    {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
    }
    @Test
    public void validateLaunchFF()
    {
        driver.get("https://www.google.com");
        System.out.println("Title : " + driver.getTitle());
        System.out.println("URL   : " + driver.getCurrentUrl());
    }

    @AfterTest
    public void tearDownBrowser()
    {
        if (driver != null) {
            driver.quit();
        }
      //  driver.close();

    }
}
