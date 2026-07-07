package com.expertrise.training.selenium.browserInteractions;
import org.openqa.selenium.WebDriver;
public class MultipleBrowserLaunchTest {
    public WebDriver driver;
    public static void main(String[] args) {
        MultipleBrowserLaunchTest test = new MultipleBrowserLaunchTest();
        test.multipleBrowserExecution("firefox");
        test.multipleBrowserExecution("edge");
        test.multipleBrowserExecution("chrome");
    }
   public void multipleBrowserExecution(String browser) {
        switch (browser) {
            case "firefox" -> {
                LaunchFireFoxBrowserTest firefox = new LaunchFireFoxBrowserTest();
                firefox.setupBrowserConfiguration();
                firefox.validateLaunchFF();
                firefox.tearDownBrowser();
            }
            case "edge" -> {
                LaunchEdgeBrowserTest edge = new LaunchEdgeBrowserTest();
                edge.setupBrowserConfiguration();
                edge.validateLaunchEdge();
                edge.tearDownBrowser();
            }

            default -> {                                // chrome
               LaunchChromeBrowerTest chrome = new LaunchChromeBrowerTest();
                chrome.setupBrowserConfiguration();
                chrome.validateLaunchChromeBrowser();
                chrome.tearDownBrowser();


            }

        }
    }
}

