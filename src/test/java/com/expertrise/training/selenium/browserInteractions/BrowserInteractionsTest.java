package com.expertrise.training.selenium.browserInteractions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

public class BrowserInteractionsTest {
    public WebDriver driver;
    @BeforeTest
    public void setupBrowserConfiguration()
    {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.google.com");
        System.out.println("Title : " + driver.getTitle());
        System.out.println("URL   : " + driver.getCurrentUrl());
    }
    @Test
    public void  BrowserNavigations()
    {
        driver.navigate().back();
        driver.navigate().forward();
        driver.navigate().refresh();
        driver.navigate().to("https://testautomationpractice.blogspot.com/");
        System.out.println("Title : " + driver.getTitle());
        System.out.println("URL   : " + driver.getCurrentUrl());
    }
    @Test
    public  void setValueInTextBox()
    {
        driver.findElement(By.id("name")).sendKeys("Test Automation");
    }

    @Test
    public void handleAlert()
    {
        WebElement button = driver.findElement(By.xpath("//button[text()='Confirmation Alert']"));
        button.click();
        driver.switchTo().alert().accept();
    }

    @Test
    public  void  fetchAllLinks()
    {
        List<WebElement> allLinks = driver.findElements(By.tagName("a"));
        for( WebElement link : allLinks)
        {
            System.out.println("Printing links of the test automation practice website --->" +link.getText());
        }
    }

    @AfterTest
    public void tearDownBrowser()
    {
        //driver.close();
        driver.quit();
    }
}
