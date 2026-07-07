package com.expertrise.training.selenium.browser.webelements;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
public class FileUpload {

    public WebDriver driver;

    @BeforeTest
    public void setupBrowserConfiguration() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://testautomationpractice.blogspot.com/");
    }

    @Test
    public void SingleFileUpload() {
        WebElement uploadElement = driver.findElement(By.id("singleFileInput"));
        uploadElement.sendKeys("D:\\Automation Sample Projects\\ExpertRiseTestAutomationFrameWork\\src\\test\\java\\com\\expertrise\\training\\selenium\\browser\\webelements\\inputFile1.txt");
        // verify the file is uploaded successfully or not
        driver.findElement(By.xpath("(//button[@type='submit'])[1]")).click();
        if( driver.findElement(By.id("singleFileStatus")).isEnabled())
        {
            driver.getPageSource().contains("inputFile1.txt");
            System.out.println("File is uploaded successfully");
        }
        else
        {
            System.out.println("File is not uploaded successfully");
        }
    }
    @Test
    public void SmultipleFileUpload() {
        WebElement multipleFileuploadElement = driver.findElement(By.id("multipleFilesInput"));
        multipleFileuploadElement.sendKeys("D:\\Automation Sample Projects\\ExpertRiseTestAutomationFrameWork\\src\\test\\java\\com\\expertrise\\training\\selenium\\browser\\webelements\\inputFile1.txt" +
                "\n D:\\Automation Sample Projects\\ExpertRiseTestAutomationFrameWork\\src\\test\\java\\com\\expertrise\\training\\selenium\\browser\\webelements\\inputFile2.txt");
        // verify the file is uploaded successfully or not
        driver.findElement(By.xpath("//button[@type='submit' and text()='Upload Multiple Files']")).click();
        if( driver.findElement(By.id("multipleFilesStatus")).isEnabled())
        {
            driver.getPageSource().contains("inputFile2.txt");
            System.out.println(" multiple Files are uploaded successfully");
        }
        else
        {
            System.out.println("File is not uploaded successfully");
        }
    }



    @AfterTest
    public void tearDownBrowser() {
        if (driver != null) {
       //     driver.quit();
        }
        //  driver.close();

    }
}
