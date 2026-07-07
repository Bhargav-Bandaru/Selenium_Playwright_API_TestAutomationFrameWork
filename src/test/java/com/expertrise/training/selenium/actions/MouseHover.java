package com.expertrise.training.selenium.actions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class MouseHover {
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
    public void mousehoverActionTest() throws Exception {
        WebElement pointmeButton = driver.findElement(By.xpath("//button[normalize-space()='Point Me']"));
        // get element color in rgba format before click and hold
        String pointMeButtonBeforeMouseHoverColour = pointmeButton.getCssValue("color");
        System.out.println("rgba code for color for element before click and hold: " + pointMeButtonBeforeMouseHoverColour);

        Actions action = new Actions(driver);
        action.moveToElement(pointmeButton).click()
                .build()
                .perform();
        Thread.sleep(5000);
        String pointMeButtonAfterMouseHoverColour = pointmeButton.getCssValue("color");
        System.out.println("rgba code for color for element before click and hold: " + pointMeButtonAfterMouseHoverColour);

    }

    @AfterTest
    public void tearDown() throws Exception {
        driver.quit();
    }

}
