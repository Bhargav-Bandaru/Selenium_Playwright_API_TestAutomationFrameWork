package com.expertrise.training.TestNG;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ParameterTest {

    @Test
    @Parameters({"browser", "url"})
    public void launchApp(String browser,
                          String url) {
        System.out.println("Browser: " + browser);
        System.out.println("URL: " + url);
        // 1. Launch browser dynamically
        // 2. Open URL from parameter
        // 3. Run test logic
    }
}
