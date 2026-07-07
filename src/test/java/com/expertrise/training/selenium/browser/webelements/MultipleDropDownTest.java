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

public class MultipleDropDownTest {
    public WebDriver driver;

    @BeforeTest
    public void setupBrowserConfiguration() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://testautomationpractice.blogspot.com/");
    }

    @Test
    public void multipleDropDownTest() {
        // Code to interact with multiple dropdowns will go here
        WebElement multipleDropDownListElement = driver.findElement(By.id("colors")); // Replace null with the actual locator for the dropdown
        Select multipleDropDown = new Select(multipleDropDownListElement);

        multipleDropDown.selectByIndex(0);
        multipleDropDown.selectByValue("blue");
        multipleDropDown.selectByVisibleText("Yellow");
        multipleDropDown.selectByVisibleText("White");

        // Deselecting the options
        multipleDropDown.deselectByValue("blue");
        multipleDropDown.deselectByVisibleText("Yellow");
        multipleDropDown.deselectByIndex(0);
        multipleDropDown.deselectAll();
        if (multipleDropDown.getAllSelectedOptions().size()==0) {
            System.out.println("All options are deselected");
        } else {
            System.out.println("Few options are still selected");
        }
    }
    @Test
    public void fetchMultipleDropDownTestSelectedOptions() {
        // Code to interact with multiple dropdowns will go here
        WebElement multipleDropDownListElement = driver.findElement(By.id("colors")); // Replace null with the actual locator for the dropdown
        Select multipleDropDown = new Select(multipleDropDownListElement);
        multipleDropDown.selectByIndex(0);
        multipleDropDown.selectByValue("blue");
        multipleDropDown.selectByVisibleText("Yellow");
        multipleDropDown.selectByVisibleText("White");
        multipleDropDown.selectByVisibleText("Red");
        if (multipleDropDown.getAllSelectedOptions().size()==0) {
            System.out.println("All options are deselected");
        } else {
            multipleDropDown.getAllSelectedOptions().forEach(option -> System.out.println("Selected option : " + option.getText()));
        }
    }
    @AfterTest
    public void tearDownBrowser() {
        if (driver != null) {
          //   driver.quit();
        }
        //  driver.close();

    }
}
