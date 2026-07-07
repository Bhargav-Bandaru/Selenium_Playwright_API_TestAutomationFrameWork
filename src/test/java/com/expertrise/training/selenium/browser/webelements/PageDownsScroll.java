package com.expertrise.training.selenium.browser.webelements;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class PageDownsScroll {
    public static void main(String[] args) throws InterruptedException {

        // Initiate the Webdriver
        WebDriver driver = new ChromeDriver();

        // adding implicit wait of 12 secs
        driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);

        //Opening the webpage where we will perform the scroll
        driver.get("https://www.tutorialspoint.com/selenium/practice/selenium_automation_practice.php");

        // JavascriptExecutor to scrolling to page bottom
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight)");

        Thread.sleep(5000);
        // access element at page bottom after scrolling
        WebElement w = driver.findElement(By.xpath("//*[@id='practiceForm']/div[11]/input"));

        System.out.println("Verify element presence after scroll down: " + w.isDisplayed());

        // quit the browser
        driver.quit();
    }
}