package com.expertrise.training.selenium.browserInteractions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class LaunchChromeBrowerTest {

public WebDriver driver;
   @BeforeTest
   public void setupBrowserConfiguration()
    {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
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
