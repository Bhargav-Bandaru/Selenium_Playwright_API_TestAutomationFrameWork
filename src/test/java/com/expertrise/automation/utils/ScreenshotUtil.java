package com.expertrise.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtil — captures Selenium WebDriver screenshots on test failure.
 *
 * <p>Screenshots are saved to {@code target/screenshots/} and returned as
 * byte[] for attachment to Cucumber HTML and Extent Reports.</p>
 */
public class ScreenshotUtil {

    private static final Logger log = LogManager.getLogger(ScreenshotUtil.class);
    private static final String SCREENSHOT_DIR = "target/screenshots/";

    static {
        new File(SCREENSHOT_DIR).mkdirs();
    }

    /**
     * Captures a screenshot using Selenium WebDriver.
     *
     * @param driver     active WebDriver instance
     * @return PNG bytes or null if capture fails
     */
    public static byte[] captureSeleniumScreenshot(WebDriver driver) {
        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            String path = SCREENSHOT_DIR + "failure_" + timestamp + ".png";
            Files.write(Paths.get(path), bytes);
            log.info("Screenshot saved: {}", path);
            return bytes;
        } catch (Exception e) {
            log.error("Failed to capture screenshot: {}", e.getMessage());
            return null;
        }
    }

    private ScreenshotUtil() {}
}
