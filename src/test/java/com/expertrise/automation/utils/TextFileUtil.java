package com.expertrise.automation.utils;

import java.io.*;
import java.nio.file.*;
import java.util.logging.Logger;

/**
 * TextFileUtil - Utility class for text file operations
 */
public class TextFileUtil {

    private static final Logger logger = Logger.getLogger(TextFileUtil.class.getName());

    /**
     * S1 - Create a text file at the given directory path
     *
     * @param fileName      Name of the file (e.g., "output.txt")
     * @param directoryPath Absolute or relative path to the directory
     * @return File object if created successfully, null otherwise
     */
    public File createTextFile(String fileName, String directoryPath) {
        try {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs(); // Create directories if not exist
            }
            File file = new File(directoryPath + File.separator + fileName);
            if (file.createNewFile()) {
                logger.info("File created: " + file.getAbsolutePath());
                return file;
            } else {
                logger.warning("File already exists: " + file.getAbsolutePath());
                return file; // Return existing file
            }
        } catch (IOException e) {
            logger.severe("Failed to create file: " + e.getMessage());
            return null;
        }
    }

    /**
     * S2 - Write (overwrite) content to a text file
     *
     * @param file    File object to write to
     * @param content Content string to write
     */
    public void writeToTextFile(File file, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            writer.write(content);
            logger.info("Content written to file: " + file.getName());
        } catch (IOException e) {
            logger.severe("Failed to write to file: " + e.getMessage());
        }
    }

    /**
     * S3 - Read all content from a text file
     *
     * @param file File object to read from
     * @return Full file content as a String, or null on failure
     */
    public String readFromTextFile(File file) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
            logger.info("File read successfully: " + file.getName());
        } catch (IOException e) {
            logger.severe("Failed to read file: " + e.getMessage());
            return null;
        }
        return content.toString().trim();
    }

    /**
     * S4 (NEW) - Append content to an existing text file without overwriting
     *
     * @param file    File object to append to
     * @param content Content string to append
     */
    public void appendToTextFile(File file, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(content);
            writer.newLine();
            logger.info("Content appended to file: " + file.getName());
        } catch (IOException e) {
            logger.severe("Failed to append to file: " + e.getMessage());
        }
    }

    /**
     * S5 (NEW) - Read a specific line from a text file (1-based index)
     *
     * @param file       File object to read from
     * @param lineNumber Line number to read (starts from 1)
     * @return The content of the specified line, or null if not found
     */
    public String readLineFromTextFile(File file, int lineNumber) {
        if (lineNumber < 1) {
            logger.warning("Line number must be >= 1");
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int currentLine = 0;
            while ((line = reader.readLine()) != null) {
                currentLine++;
                if (currentLine == lineNumber) {
                    logger.info("Line " + lineNumber + " read from: " + file.getName());
                    return line;
                }
            }
            logger.warning("File has fewer than " + lineNumber + " lines.");
        } catch (IOException e) {
            logger.severe("Failed to read line from file: " + e.getMessage());
        }
        return null;
    }
}