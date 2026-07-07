package com.expertrise.training.selenium.actions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class ClickAction {

    public WebDriver driver;

    @BeforeTest
    public void setUp() throws Exception {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.navigate().to("https://www.tutorialspoint.com/selenium/practice/buttons.php");

    }

    @Test(enabled = false)

    public void singleClickTest() throws Exception {
        WebElement clickMe = driver.findElement(By.xpath("//button[normalize-space()='Click Me']"));
        Actions action = new Actions(driver);
        action.moveToElement(clickMe).click();
        Thread.sleep(3000);
    }

    @Test(enabled = false)
    public void rightClickTest() throws Exception {
        WebElement rightClickMe = driver.findElement(By.xpath("//button[normalize-space()='Right Click Me']"));
        Actions action = new Actions(driver);
        action.contextClick(rightClickMe).build().perform();
        Thread.sleep(3000);
    }

    @Test(enabled = true)
    public void doubleClickMe() throws Exception {
        WebElement doubleClickMe = driver.findElement(By.xpath("//button[normalize-space()='Double Click Me']"));
        Actions action = new Actions(driver);
        action.contextClick(doubleClickMe).build().perform();
        Thread.sleep(3000);
    }


    @Test(enabled =false)
    public void doubleClickTest() throws Exception {

        driver.get("https://testautomationpractice.blogspot.com/");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement copyText = driver.findElement(By.xpath("//button[normalize-space()='Copy Text']"));
        Actions action = new Actions(driver);
        action.doubleClick(copyText).perform();
        Thread.sleep(3000);
    }




    @AfterTest
    public void tearDown() throws Exception {
        driver.quit();
    }

}
