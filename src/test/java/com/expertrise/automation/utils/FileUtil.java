package com.expertrise.automation.utils;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * FileUtil - Utility class for file and directory operations
 */
public class FileUtil {

    private static final Logger logger = Logger.getLogger(FileUtil.class.getName());

    /**
     * S1 - Recursively delete a directory and all its contents
     *
     * @param fileDirectory Root directory to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteDirectory(File fileDirectory) {
        if (!fileDirectory.exists()) {
            logger.warning("Directory does not exist: " + fileDirectory.getAbsolutePath());
            return false;
        }
        try {
            Files.walkFileTree(fileDirectory.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            logger.info("Directory deleted: " + fileDirectory.getAbsolutePath());
            return true;
        } catch (IOException e) {
            logger.severe("Failed to delete directory: " + e.getMessage());
            return false;
        }
    }

    /**
     * S2 - Create a folder/directory at the given path (including parent dirs)
     *
     * @param path Full path of the directory to create
     * @return true if created successfully, false otherwise
     */
    public boolean createDirectory(String path) {
        File directory = new File(path);
        if (directory.exists()) {
            logger.warning("Directory already exists: " + path);
            return true;
        }
        boolean created = directory.mkdirs();
        if (created) {
            logger.info("Directory created: " + path);
        } else {
            logger.severe("Failed to create directory: " + path);
        }
        return created;
    }

    /**
     * S3 - Find the root directory of the Maven project (where pom.xml exists)
     *
     * @return Absolute path of the root directory, or null if not found
     */
    public String findRootDirectory() {
        File current = new File(System.getProperty("user.dir"));
        while (current != null) {
            File pom = new File(current, "pom.xml");
            if (pom.exists()) {
                logger.info("Root directory found: " + current.getAbsolutePath());
                return current.getAbsolutePath();
            }
            current = current.getParentFile();
        }
        logger.warning("pom.xml not found — could not determine root directory.");
        return null;
    }

    /**
     * S4 - Delete all files in a directory recursively (keeps folder structure)
     *
     * @param directoryPath Path of the directory to clean up
     */
    public void deletePreviousExecutionFiles(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            logger.warning("Directory not found: " + directoryPath);
            return;
        }
        File[] files = directory.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) {
                deletePreviousExecutionFiles(file.getAbsolutePath()); // Recurse
                file.delete(); // Delete empty folder
            } else {
                file.delete();
                logger.info("Deleted file: " + file.getName());
            }
        }
    }

    /**
     * S5 - Check whether a file or directory exists at the given path
     *
     * @param path File or directory path to check
     * @return true if exists, false otherwise
     */
    public boolean isFilePresent(String path) {
        File file = new File(path);
        boolean exists = file.exists();
        logger.info("File presence check [" + path + "]: " + exists);
        return exists;
    }

    /**
     * S6 (NEW) - Copy a file from source path to destination path
     *
     * @param sourcePath      Full path of the source file
     * @param destinationPath Full path of the destination (including filename)
     * @return true if copied successfully, false otherwise
     */
    public boolean copyFile(String sourcePath, String destinationPath) {
        try {
            Path source = Paths.get(sourcePath);
            Path destination = Paths.get(destinationPath);
            // Create parent directories if they don't exist
            Files.createDirectories(destination.getParent());
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            logger.info("File copied from " + sourcePath + " to " + destinationPath);
            return true;
        } catch (IOException e) {
            logger.severe("Failed to copy file: " + e.getMessage());
            return false;
        }
    }

    /**
     * S7 (NEW) - Rename a file to a new name within the same directory
     *
     * @param file    File object to rename
     * @param newName New name for the file (just the name, not full path)
     * @return true if renamed successfully, false otherwise
     */
    public boolean renameFile(File file, String newName) {
        if (!file.exists()) {
            logger.warning("File does not exist: " + file.getAbsolutePath());
            return false;
        }
        File renamedFile = new File(file.getParent() + File.separator + newName);
        boolean renamed = file.renameTo(renamedFile);
        if (renamed) {
            logger.info("File renamed to: " + newName);
        } else {
            logger.severe("Failed to rename file: " + file.getName());
        }
        return renamed;
    }

    /**
     * S8 (NEW) - List all files in a directory matching a given extension
     *
     * @param dirPath Full path to the directory
     * @param ext     File extension to filter (e.g., ".txt", ".java")
     * @return List of matching File objects (empty list if none found)
     */
    public List<File> listFilesByExtension(String dirPath, String ext) {
        List<File> matchingFiles = new ArrayList<>();
        File directory = new File(dirPath);
        if (!directory.exists() || !directory.isDirectory()) {
            logger.warning("Invalid directory: " + dirPath);
            return matchingFiles;
        }
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(ext.toLowerCase()));
        if (files != null) {
            for (File f : files) {
                matchingFiles.add(f);
                logger.info("Found file: " + f.getName());
            }
        }
        return matchingFiles;
    }
}