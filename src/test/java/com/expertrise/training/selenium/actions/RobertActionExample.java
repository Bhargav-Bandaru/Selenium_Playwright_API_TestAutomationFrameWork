package com.expertrise.training.selenium.actions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.awt.*;

public class RobertActionExample {

public WebDriver driver ;
    @BeforeTest
    public void setUp() throws Exception {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.navigate().to("https://www.tutorialspoint.com/selenium/practice/register.php");

    }


    @Test
    public void RobertTest() {


        WebElement firstName = driver.findElement(By.xpath("//*[@id='firstname']"));
        firstName.sendKeys("expertRise");
        WebElement lastName = driver.findElement(By.xpath("//*[@id='lastname']"));

        try {
            Robot robot = new Robot();
            robot.setAutoDelay(100);

            // ensure focus on firstName
            firstName.click();

            // select all (Ctrl + A)
            robot.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
            robot.keyPress(java.awt.event.KeyEvent.VK_A);
            robot.keyRelease(java.awt.event.KeyEvent.VK_A);
            robot.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);

            // copy (Ctrl + C)
            robot.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
            robot.keyPress(java.awt.event.KeyEvent.VK_C);
            robot.keyRelease(java.awt.event.KeyEvent.VK_C);
            robot.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);

            // tab to next input
            robot.keyPress(java.awt.event.KeyEvent.VK_TAB);
            robot.keyRelease(java.awt.event.KeyEvent.VK_TAB);

            // paste (Ctrl + V)
            robot.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
            robot.keyPress(java.awt.event.KeyEvent.VK_V);
            robot.keyRelease(java.awt.event.KeyEvent.VK_V);
            robot.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String text = lastName.getAttribute("value");
        System.out.println("Value copied from firstName and pasted in to last name is --->: " + text);

    }

    @AfterTest
    public void tearDown() throws Exception {
        driver.quit();
    }
}
