package com.expertrise.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;

/**
 * TestDataUtil — loads test data key-value pairs from testdata.properties.
 *
 * Usage: TestDataUtil.get("validUser.email")
 */
public class TestDataUtil {

    private static final Logger log = LogManager.getLogger(TestDataUtil.class);
    private static final Properties data = new Properties();
    private static final String DATA_FILE = "src/test/resources/testdata/testdata.properties";

    static {
        try (InputStream is = new FileInputStream(DATA_FILE)) {
            data.load(is);
            log.info("Test data loaded from: {}", DATA_FILE);
        } catch (IOException e) {
            log.warn("testdata.properties not found at {}", DATA_FILE);
        }
    }

    /** Gets a test data value by key. Throws if not found. */
    public static String get(String key) {
        String value = data.getProperty(key);
        if (value == null) throw new RuntimeException("Test data key not found: " + key);
        return value.trim();
    }

    /** Gets a test data value with a default fallback. */
    public static String get(String key, String defaultValue) {
        return data.getProperty(key, defaultValue).trim();
    }

    private TestDataUtil() {}
}
