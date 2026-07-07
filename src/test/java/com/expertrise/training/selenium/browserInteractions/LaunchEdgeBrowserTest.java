package com.expertrise.training.selenium.browserInteractions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class LaunchEdgeBrowserTest {
    public WebDriver driver;
    @BeforeTest
    public void setupBrowserConfiguration()
    {
       // WebDriverManager.edgedriver().setup();
        System.setProperty("webdriver.edge.driver", "C:\\Users\\admin\\OneDrive\\Documents\\edgedriver_win64\\msedgedriver.exe");
        driver = new EdgeDriver();
        driver.manage().window().maximize();
    }
    @Test
    public void validateLaunchEdge()
    {
        driver.get("https://www.google.com");
        System.out.println("Title : " + driver.getTitle());
        System.out.println("URL   : " + driver.getCurrentUrl());
    }

    @AfterTest
    public void tearDownBrowser()
    {
        if (driver != null) {
            driver.quit();
        }
        //  driver.close();

    }

}
