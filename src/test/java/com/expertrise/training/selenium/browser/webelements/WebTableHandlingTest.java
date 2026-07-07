package com.expertrise.training.selenium.browser.webelements;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class WebTableHandlingTest {
    public WebDriver driver;
    @BeforeTest
    public void setupBrowserConfiguration() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://testautomationpractice.blogspot.com/");
    }
    @Test
    public void validateWebTableHandling() throws IOException {
        String expectedValue ="System";
        ////h2[text()='Dynamic Web Table']/..//div[@class='widget-content']//table[@id='taskTable']
        WebElement webtable = driver.findElement(By.xpath("//table[@id='taskTable']"));
        // Fetching the number of rows in the web table
        int rowCount = webtable.findElements(By.tagName("tr")).size();
        for (int i = 1; i < rowCount; i++) {
            // fetching the all rows of the web table and iterating through each row
            WebElement rows = webtable.findElements(By.tagName("tr")).get(i);
            // Fetching the number of columns in the web table
            int colCount = rows.findElements(By.tagName("td")).size();
            for (int j = 0; j < colCount; j++) {
                ////h2[text()='Dynamic Web Table']/..//div[@class='widget-content']//table[@id='taskTable']//tr[1]/td[1]
                String cellValue = rows.findElements(By.tagName("td")).get(j).getText();
                if (cellValue.equals(expectedValue)) {
                    System.out.println("Found the expected value: " + expectedValue + " at row: " + i + ", column: " + j);
                    return;
                }
            }
        }

    }
    @AfterTest
    public void tearDownBrowser() {
        {
            try {
                File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
                File destFile = new File("C:\\Users\\bhargavbandaru\\Downloads\\webtable.png");
                // Create parent directories if they don't exist
                destFile.getParentFile().mkdirs();
                FileUtils.copyFile(scrFile, destFile);
                System.out.println("Screenshot saved successfully at: " + destFile.getAbsolutePath());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (driver != null) {
             driver.quit();
        }
        //  driver.close();

    }
}
