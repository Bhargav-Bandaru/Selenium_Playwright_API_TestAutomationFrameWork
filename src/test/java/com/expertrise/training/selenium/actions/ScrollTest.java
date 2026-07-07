package com.expertrise.training.selenium.actions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class ScrollTest {

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
    public void HorizontalScroll() throws Exception {

        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("window.scrollBy(20000,0)", "");
        Thread.sleep(3000);

    }
    @Test
    public void verticalScroll() throws Exception {

        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("window.scrollBy(0,600)", "");
        Thread.sleep(3000);

    }
    @Test
    public void verticalScrollToBottom() throws Exception {

        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight)", "");
      //  javascriptExecutor.executeScript("window.scrollTo(document.body.scrollHeight, 0)", "");

        Thread.sleep(3000);
    }
    @Test
    public void verticalScrollToTop() throws Exception {

        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("window.scrollTo(document.body.scrollHeight, 0)", "");
        Thread.sleep(3000);
    }
    @Test
    public void scrollToElement() throws Exception {
        WebElement elementTobeScrolled = driver.findElement(By.xpath("//a[text()='Blog']"));
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView();",elementTobeScrolled);
        Thread.sleep(3000);
    }
    @AfterTest
    public void tearDown() throws Exception {
        driver.quit();
    }
}
