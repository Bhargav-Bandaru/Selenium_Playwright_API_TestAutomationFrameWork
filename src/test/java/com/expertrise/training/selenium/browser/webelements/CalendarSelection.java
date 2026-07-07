package com.expertrise.training.selenium.browser.webelements;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class CalendarSelection {
    private WebDriver driver;
    private String baseUrl;

    @BeforeTest
    public void setUp() throws Exception {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://testautomationpractice.blogspot.com/");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void calender1Selection() throws Exception {
        WebElement element = driver.findElement(By.xpath("//input[@id='datepicker']"));
        element.sendKeys("06/17/2026");
        driver.findElement(By.xpath("//input[@id='start-date']")).click();

    }
    @Test
    public void calender2Selection() throws Exception {
        WebElement DatePicker2 = driver.findElement(By.xpath("//input[@id='txtDate']"));
        DatePicker2.click();
        Thread.sleep(3000);
        driver.findElement(By.xpath("(//a[normalize-space()='18'])[1]")).click();
        Thread.sleep(3000);
        driver.findElement(By.xpath("//input[@id='start-date']")).click();

    }

    @AfterTest
    public void tearDown() throws Exception {
        Thread.sleep(3000);
        driver.quit();
    }
}
