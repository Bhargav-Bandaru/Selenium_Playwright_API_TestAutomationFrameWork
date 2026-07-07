package com.expertrise.training.selenium.browser.webelements;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class FileDownloadTest {

    public WebDriver driver;
    public WebDriverWait wait;

    // ─── Download folder path ─────────────────────────────────────────────────
    String downloadPath = System.getProperty("user.dir") + "\\downloads";
    String expectedFileName = "sampleFile.jpeg"; // from download="sampleFile.jpeg" in HTML

    @BeforeTest
    public void setupBrowserConfiguration() {

        // ─── STEP 1: Create download folder if not exists ─────────────────────
        File downloadDir = new File(downloadPath);
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
            System.out.println("Download folder created: " + downloadPath);
        }

        // ─── STEP 2: Chrome preferences for auto-download ─────────────────────
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadPath);   // Set download path
        prefs.put("download.prompt_for_download", false);        // No "Save As" dialog
        prefs.put("download.directory_upgrade", true);
        prefs.put("safebrowsing.enabled", true);                 // Avoid unsafe warning
        prefs.put("plugins.always_open_pdf_externally", true);   // Download PDF directly

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        System.out.println("Browser launched with download path: " + downloadPath);
    }

    @Test
    public void validateFileDownload() throws InterruptedException {

        // ─── STEP 3: Delete file if already exists from previous run ──────────
        File existingFile = new File(downloadPath + "\\" + expectedFileName);
        if (existingFile.exists()) {
            existingFile.delete();
            System.out.println("Deleted existing file before test: " + expectedFileName);
        }

        // ─── STEP 4: Navigate to the page ─────────────────────────────────────
        driver.get("https://www.tutorialspoint.com/selenium/practice/upload-download.php");
        System.out.println("Page Title : " + driver.getTitle());
        System.out.println("Page URL   : " + driver.getCurrentUrl());

        // ─── STEP 5: Wait for Download button and click ───────────────────────
        WebElement downloadBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("downloadButton")));

        System.out.println("Download button found: " + downloadBtn.getText());
        downloadBtn.click();
        System.out.println("Clicked Download button");

        // ─── STEP 6: Wait for file to download completely ─────────────────────
        System.out.println("Waiting for file to download...");
        boolean isDownloaded = waitForFileDownload(expectedFileName, 30);

        // ─── STEP 7: Verify file exists ───────────────────────────────────────
        File downloadedFile = new File(downloadPath + "\\" + expectedFileName);

        System.out.println("---------------------------------------------------");
        System.out.println("File Name     : " + downloadedFile.getName());
        System.out.println("File Path     : " + downloadedFile.getAbsolutePath());
        System.out.println("File Exists   : " + downloadedFile.exists());
        System.out.println("File Size     : " + downloadedFile.length() + " bytes");
        System.out.println("Download Status: " + (isDownloaded ? "SUCCESS" : "FAILED"));
        System.out.println("---------------------------------------------------");

        // ─── STEP 8: Assertions ───────────────────────────────────────────────
        Assert.assertTrue(isDownloaded,
                "FAIL: File was NOT downloaded within the timeout period!");
        Assert.assertTrue(downloadedFile.exists(),
                "FAIL: File does not exist at path: " + downloadedFile.getAbsolutePath());
        Assert.assertTrue(downloadedFile.length() > 0,
                "FAIL: Downloaded file is empty!");

        System.out.println("PASS: File downloaded successfully!");
    }

    // ─── HELPER: Wait until file is fully downloaded (no .crdownload temp file) ──
    private boolean waitForFileDownload(String fileName, int timeoutSeconds)
            throws InterruptedException {

        File downloadDir = new File(downloadPath);
        long endTime = System.currentTimeMillis() + (timeoutSeconds * 1000L);

        while (System.currentTimeMillis() < endTime) {
            File[] files = downloadDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    // .crdownload = Chrome temp file (still downloading)
                    // .tmp        = Edge/Firefox temp file (still downloading)
                    if (file.getName().equals(fileName)
                            && !file.getName().endsWith(".crdownload")
                            && !file.getName().endsWith(".tmp")) {
                        System.out.println("File download completed: " + file.getName());
                        return true;
                    }
                }
            }
            Thread.sleep(1000); // Check every 1 second
            System.out.println("Waiting... checking for file: " + fileName);
        }
        return false; // Timed out
    }

    @AfterTest
    public void tearDownBrowser() {
        if (driver != null) {
            driver.quit();
            System.out.println("Browser closed");
        }
    }
}