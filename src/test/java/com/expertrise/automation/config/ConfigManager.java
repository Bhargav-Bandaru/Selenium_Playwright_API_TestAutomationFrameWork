package com.expertrise.automation.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigManager — single source of truth for all framework configuration.
 *
 * <p>Loads from {@code src/test/resources/config/config.properties}.
 * System properties (-D flags) always override file values, enabling
 * CI/CD pipelines to inject environment-specific config without code changes.</p>
 *
 * <p>Usage:
 * <pre>
 *   String url    = ConfigManager.get("base.url");
 *   String browser = ConfigManager.get("browser", "chrome");  // with default
 * </pre>
 */
public class ConfigManager {

    private static final Logger log = LogManager.getLogger(ConfigManager.class);
    private static final Properties props = new Properties();
    private static final String CONFIG_FILE = "src/test/resources/config/config.properties";

    static {
        loadProperties();
    }

    private static void loadProperties() {
        // 1. Load from file
        try (InputStream is = new FileInputStream(CONFIG_FILE)) {
            props.load(is);
            log.info("Loaded configuration from: {}", CONFIG_FILE);
        } catch (IOException e) {
            log.warn("config.properties not found at {}. Using system properties only.", CONFIG_FILE);
        }

        // 2. System properties (-D flags) override file values
        System.getProperties().forEach((k, v) -> props.setProperty(k.toString(), v.toString()));
    }

    /**
     * Gets a config value by key.
     * @param key property key
     * @return value or throws if not found
     */
    public static String get(String key) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new RuntimeException("Config key '" + key + "' not found in config.properties or system properties.");
        }
        return value.trim();
    }

    /**
     * Gets a config value with a fallback default.
     * @param key          property key
     * @param defaultValue returned when key is absent
     */
    public static String get(String key, String defaultValue) {
        String value = props.getProperty(key);
        return (value != null && !value.isBlank()) ? value.trim() : defaultValue;
    }

    /** Gets an int config value with default. */
    public static int getInt(String key, int defaultValue) {
        try { return Integer.parseInt(get(key)); }
        catch (Exception e) { return defaultValue; }
    }

    /** Gets a boolean config value (true/false). */
    public static boolean getBoolean(String key, boolean defaultValue) {
        try { return Boolean.parseBoolean(get(key)); }
        catch (Exception e) { return defaultValue; }
    }

    /** Convenience — base URL for the application under test. */
    public static String getBaseUrl() { return get("base.url", "https://automationexercise.com"); }

    /** Convenience — default browser name. */
    public static String getBrowser() { return get("browser", "chrome"); }

    /** Convenience — API base URI. */
    public static String getApiBaseUrl() { return get("api.base.url", "http://localhost:3000"); }

    private ConfigManager() { /* utility class */ }
}
