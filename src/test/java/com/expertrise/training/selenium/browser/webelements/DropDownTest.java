package com.expertrise.training.selenium.browser.webelements;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class DropDownTest {

    public WebDriver driver;

    @BeforeTest
    public void setupBrowserConfiguration() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://testautomationpractice.blogspot.com/");
    }

    @Test
    public void DropDownTest() throws InterruptedException {
        // Code to verify dropdown functionality will go here
        Select dropdown = new Select(driver.findElement(By.id("country")));
        dropdown.selectByIndex(0); // Select the first option
        Thread.sleep(1000);
        dropdown.selectByValue("india"); // Select option by value
        Thread.sleep(1000);
        dropdown.selectByVisibleText("Canada"); // Select option by visible text
        Thread.sleep(1000);
        dropdown.selectByValue("australia");
      //  dropdown.deselectAll();
    }

    @Test
    public void getDropDownOptions() throws InterruptedException {
        // Code to verify dropdown functionality will go here
        Select dropdown = new Select(driver.findElement(By.id("country")));
        System.out.println("Printing all the options of the dropdown");
        for (WebElement option : dropdown.getOptions()) {
            System.out.println("Printing the country DDL options ----> " +option.getText());
        }
    }

    @AfterTest
    public void tearDownBrowser() {
        if (driver != null) {
            //    driver.quit();
        }
        driver.close();

    }
}
