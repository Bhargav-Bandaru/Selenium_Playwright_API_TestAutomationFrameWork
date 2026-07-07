package com.expertrise.training.selenium.browser.webelements;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ScreenShotCapture {
    public static void main(String[] args) throws InterruptedException, IOException {
        // Initiate the Webdriver
        WebDriver driver = new ChromeDriver();
        // adding implicit wait of 15 secs
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        // Opening the webpage where we will identify an element
        driver.get("https://www.tutorialspoint.com/selenium/practice/selenium_automation_practice.php");
        // Identify the input box with xpath locator
        WebElement e = driver.findElement
                (By.xpath("//*[@id='name']"));
        // enter text in input box
        e.sendKeys("Selenium");
        // Get the value entered
        String text = e.getAttribute("value");
        System.out.println("Entered text is: " + text);
        // take screenshot of input box and store in project location
        File scrFile = e.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new  File("C:\\Users\\admin\\Downloads\\ImageElement.png"));
        // Closing browser
        driver.quit();
    }
}
