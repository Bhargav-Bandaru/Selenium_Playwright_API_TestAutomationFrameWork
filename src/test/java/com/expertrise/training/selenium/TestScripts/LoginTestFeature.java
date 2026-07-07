package com.expertrise.training.selenium.TestScripts;


import com.expertrise.automation.config.DriverFactory;
import com.expertrise.automation.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class LoginTestFeature {

    public WebDriver driver;
    LoginPage loginPage =new LoginPage();;

    @BeforeTest
    public void setupBrowserConfiguration(String browserType)
    {

        DriverFactory.initSeleniumDriver("chrome");
     /*   WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();*/
        driver.get("https://automationexercise.com");
        System.out.println("Title : " + driver.getTitle());
        System.out.println("URL   : " + driver.getCurrentUrl());
    }


    @Test
    @Parameters({"browserType","userName","password"})
    public void testLoginFeature(String userName, String password ) {

        loginPage.navigateToLoginPage();
        loginPage.enterLoginEmail(userName);
        loginPage.enterLoginPassword(password);
        loginPage.clickLoginButton();
        loginPage.clickLogout();
    }
    @AfterTest
    public void tearDownBrowser()
    {
        //driver.close();
        driver.quit();
    }
}
