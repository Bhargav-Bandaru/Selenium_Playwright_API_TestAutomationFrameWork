package com.expertrise.training.selenium.browser.webelements;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class WebElementsMethodsTest {
    public WebDriver driver;

    @BeforeTest
    public void setupBrowserConfiguration() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void exploreWebElementsInPage() {
        //launch the browser and navigate to the URL
        driver.get("https://testautomationpractice.blogspot.com/");
        System.out.println("Title : " + driver.getTitle());
        System.out.println("URL   : " + driver.getCurrentUrl());

        WebElement guiElement = driver.findElement(By.xpath("//h1[contains(text(),'Automation Testing Practice')]"));
        System.out.println("Color of the element is : " + guiElement.getCssValue("color"));

        // handling input text box
        WebElement inputTextBox = driver.findElement(By.id("name"));

        // getting width and size of the pixel
        int width = inputTextBox.getSize().getWidth();
        int height = inputTextBox.getSize().getHeight();
        System.out.println("Width  and pixel of the input text box is : " + width + " pixels");

        // check element is displayed or not using isDisplayed() method
        if (inputTextBox.isDisplayed()) {
            System.out.println("Input Text Box is displayed");
            inputTextBox.sendKeys("TestName");
        }

        driver.findElement(By.id("email")).sendKeys("TestEmail");
        driver.findElement(By.cssSelector("#phone")).clear();

        WebElement phoneInput = driver.findElement(By.cssSelector("#phone"));
        // check element is enabled or not using isEnabled() method
        if (phoneInput.isEnabled()) {
            phoneInput.sendKeys("687686");
        }

        // handling  radio buttons using click() method

        // check element is selected or not using isSelected() method
        WebElement radioButton = driver.findElement(By.xpath("//label[@class='form-check-label' and text()='Male']"));
        if (!radioButton.isSelected()) {
            radioButton.click();
        }
        // handling checkboxes  using click() method

        driver.findElement(By.xpath("//label[@class='form-check-label' and text()='Monday']")).click();

        // get text of an element using getText() method
        String text = driver.findElement(By.xpath("//h1[contains(text(),'Automation Testing Practice')]")).getText();
        System.out.println("Text of the element is : " + text);

        // get attribute value of an element using getAttribute() method
        String attributeValue = driver.findElement(By.id("name")).getAttribute("placeholder");
        System.out.println("Attribute value of the element is : " + attributeValue);
    }

    @AfterTest
    public void tearDownBrowser() {
        if (driver != null) {
            driver.quit();
        }
        //  driver.close();

    }
}
