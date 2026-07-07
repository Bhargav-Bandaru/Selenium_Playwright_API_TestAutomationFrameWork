package com.expertrise.training.selenium.browserInteractions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class FirstTest {
    public static void main(String[] args)
    {
        WebDriverManager.chromedriver().setup();  // auto driver management
       /* WebDriverManager.edgedriver().setup();
        WebDriverManager.firefoxdriver().setup();*/
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.google.com");
        System.out.println("Title : " + driver.getTitle());
        System.out.println("URL   : " + driver.getCurrentUrl());
        driver.quit();  // closes ALL windows
    }
}
