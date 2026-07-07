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

public class DragAndDropTest {

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
    public void DragAndDrop() throws Exception {
        Actions action = new Actions(driver);
        WebElement SourceElement = driver.findElement(By.xpath("//div[@id='draggable']"));
        WebElement TargetElement = driver.findElement(By.xpath("//div[@id='droppable']"));

        action.dragAndDrop(SourceElement, TargetElement).build().perform();
//        action.clickAndHold(SourceElement)
//                .moveToElement(TargetElement)
//                .release(TargetElement)
//                .build().perform();
       WebElement afterDrop = driver.findElement(By.xpath("//div[@id='droppable']/p"));
        String afterDropText = afterDrop.getText();
        System.out.println("After drop text: " + afterDropText);

    }

    @AfterTest
    public void tearDown() throws Exception {
        driver.quit();
    }
}
