package com.expertrise.training.selenium.browserInteractions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ElementInteractionTest {
    public WebDriver driver;

    @BeforeTest
    public void setupBrowserConfiguration() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        //launch the browser and navigate to the URL
        driver.get("https://testautomationpractice.blogspot.com/");
        System.out.println("Title : " + driver.getTitle());
        System.out.println("URL   : " + driver.getCurrentUrl());
    }

    @Test
    public void fetchWebElementCSSValue() {
        WebElement guiElement = driver.findElement(By.xpath("//h1[contains(text(),'Automation Testing Practice')]"));
        System.out.println("Color of the element is : " + guiElement.getCssValue("color"));
    }

    @Test (priority = 4)
    public void getwidthAndHeightOfWebElement() {
        WebElement guiElement = driver.findElement(By.xpath("//h1[contains(text(),'Automation Testing Practice')]"));
        int width = guiElement.getSize().getWidth();
        int height = guiElement.getSize().getHeight();
        System.out.println("Width  and pixel of the element is : " + width + " pixels");
        System.out.println("Height and pixel of the element is : " + height + " pixels");
    }

    @Test (priority = 6)
    public void getLocationOfWebElement() {
        WebElement guiElement = driver.findElement(By.xpath("//h1[contains(text(),'Automation Testing Practice')]"));
        int xCoordinate = guiElement.getLocation().getX();
        int yCoordinate = guiElement.getLocation().getY();
        System.out.println("X coordinate of the element is : " + xCoordinate);
        System.out.println("Y coordinate of the element is : " + yCoordinate);
    }

    @Test (priority = 8)
    public void checkWebElementIsDisplayed() {
        WebElement inputTextBox = driver.findElement(By.id("name"));
        if (inputTextBox.isDisplayed()) {
            System.out.println("The element is displayed");
        } else {
            System.out.println("The element is not displayed");
        }
    }

    @Test (priority = 2)
    public void clickRadioButton() {
        WebElement radioButton = driver.findElement(By.xpath("//label[@class='form-check-label' and text()='Male']"));
       // check the radio button is selected or not using isSelected() method
        if (!radioButton.isSelected()) {
            radioButton.click();
            System.out.println("The Radio button is selected");
        }
        else {
            System.out.println("The Radio button is already selected");
        }
    }

    @Test (priority = 1)
    public void clickCheckBox() {
        WebElement checkBox = driver.findElement(By.xpath("//label[@class='form-check-label' and text()='Monday']"));
        // check the checkbox is selected or not using isSelected() method
        if (!checkBox.isSelected()) {
            checkBox.click();
            System.out.println("The Check box is selected");
        }
        else {
            System.out.println("The Check box is already selected");
        }
    }

    @Test (priority = 9)
    public void  getTextOfWebElement() {
        WebElement guiElement = driver.findElement(By.xpath("//h1[contains(text(),'Automation Testing Practice')]"));
        String text = guiElement.getText();
        System.out.println("Text of the element is : " + text);
    }

    @Test (priority = 10)
    public void getAttributeValueOfWebElement() {
        WebElement guiElement = driver.findElement(By.id("name"));
        String attributeValue = guiElement.getAttribute("placeholder");
        System.out.println("Attribute value of the element is : " + attributeValue);
    }


    @Test (priority = 0)
    public void setInputValuesToInputField() {

        // setting the value of an input field using sendKeys() method
        driver.findElement(By.id("name")).sendKeys("Test Name");
        driver.findElement(By.id("email")).sendKeys("TestEmail");
        driver.findElement(By.cssSelector("#phone")).clear();
        WebElement phoneInput = driver.findElement(By.cssSelector("#phone"));
        // check element is enabled or not using isEnabled() method
        if (phoneInput.isEnabled()) {
            phoneInput.sendKeys("687686");
        }
    }

    @AfterTest
    public void tearDownBrowser() {
        if (driver != null) {
            driver.quit();
        }
        //  driver.close();

    }
}
