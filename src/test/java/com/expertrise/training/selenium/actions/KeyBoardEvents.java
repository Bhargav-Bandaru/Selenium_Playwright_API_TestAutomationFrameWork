package com.expertrise.training.selenium.actions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class KeyBoardEvents {

/*
    perform keyboard events operations
     like key up,
      and down,
      enter multiple characters in other operations,
      and copy and paste operations using the Actions class
 /*
    Methods

keyDown(CharSequence key) − perform a modifier key press, passed as a parameter.
keyDown(WebElement e, CharSequence key) − perform a modifier key press post focusing an element. The webElement e, and key to be pressed are passed as parameters.
keyUp(CharSequence key) − perform a modifier key release, passed as a parameter.
keyUp(WebElement e, CharSequence key) − perform a modifier key release post focusing an element. The webElement e, and key to be released are passed as parameters.
sendKeys(CharSequence key) −  to send keys to elements in focus. The key to be sent is passed as a parameter.
sendKeys(WebElement e, CharSequence key) −  to send keys to the webElement passed as parameter.
build() − This method is used to create a combination of actions having all the actions to be carried on.
perform() − This method is used to perform actions without invoking the build() first.


     */

    public WebDriver driver;

    @BeforeTest
    public void setUp() throws Exception {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.navigate().to("https://www.tutorialspoint.com/selenium/practice/register.php");

    }

    @Test
    public void copyPasteActionTest() {

        WebElement firstName = driver.findElement(By.xpath("//*[@id='firstname']"));
        firstName.sendKeys("expertRise");
        WebElement lastName = driver.findElement(By.xpath("//*[@id='lastname']"));
        //choosing the keys

        // chose the key as per platform

        // object of Actions class to copy then paste
        Actions actions = new Actions(driver);
        actions.keyDown(Keys.CONTROL);
        actions.sendKeys("a");
        actions.keyDown(Keys.CONTROL);
        actions.build().perform();

        //copy

        // Actions class methods to copy text
        actions.keyDown(Keys.CONTROL);
        actions.sendKeys("c");
        actions.keyUp(Keys.CONTROL);
        actions.build().perform();

        // tab and reach to next input box
        actions.sendKeys(Keys.TAB);
        actions.build().perform();

        // to paste text
        actions.keyDown(Keys.CONTROL);
        actions.sendKeys("v");
        actions.keyUp(Keys.CONTROL);
        actions.build().perform();

        String text = lastName.getAttribute("value");
        System.out.println("Value copied from firstName and pasted in to last name is --->: " + text);

    }


    @AfterTest
    public void tearDown() throws Exception {
        driver.quit();
    }
}
