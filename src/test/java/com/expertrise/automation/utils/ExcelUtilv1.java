package com.expertrise.automation.utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * ExcelUtil - Utility class for reading and writing Excel (.xlsx) files
 * Dependency: Apache POI (add to pom.xml)
 *
 * <dependency>
 *   <groupId>org.apache.poi</groupId>
 *   <artifactId>poi-ooxml</artifactId>
 *   <version>5.2.3</version>
 * </dependency>
 */
public class ExcelUtilv1 {

    private static final Logger logger = Logger.getLogger(ExcelUtil.class.getName());

    // ─── Helper: open workbook ───────────────────────────────────────────────

    private Workbook openWorkbook(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(new File(filePath));
        return new XSSFWorkbook(fis);
    }

    private void saveWorkbook(Workbook workbook, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
    }

    /**
     * Helper - Get cell value as String regardless of cell type
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }

    // ─── S1 ─────────────────────────────────────────────────────────────────

    /**
     * S1 (NEW) - Read a cell value from an Excel file by sheet name, row, and column
     *
     * @param filePath  Full path to the .xlsx file
     * @param sheetName Name of the sheet
     * @param rowNum    Row index (0-based)
     * @param colNum    Column index (0-based)
     * @return Cell value as a String, or empty string if cell is blank/null
     */
    public String getCellValue(String filePath, String sheetName, int rowNum, int colNum) {
        try (Workbook workbook = openWorkbook(filePath)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.warning("Sheet not found: " + sheetName);
                return "";
            }
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                logger.warning("Row not found at index: " + rowNum);
                return "";
            }
            String value = getCellValueAsString(row.getCell(colNum));
            logger.info("getCellValue [" + sheetName + "][" + rowNum + "][" + colNum + "]: " + value);
            return value;
        } catch (IOException e) {
            logger.severe("Failed to read cell value: " + e.getMessage());
            return "";
        }
    }

    // ─── S2 ─────────────────────────────────────────────────────────────────

    /**
     * S2 (NEW) - Write a value to a specific cell in an Excel file
     *
     * @param filePath  Full path to the .xlsx file
     * @param sheetName Name of the sheet
     * @param rowNum    Row index (0-based)
     * @param colNum    Column index (0-based)
     * @param value     String value to write into the cell
     */
    public void setCellValue(String filePath, String sheetName, int rowNum, int colNum, String value) {
        try (Workbook workbook = openWorkbook(filePath)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.warning("Sheet not found: " + sheetName);
                return;
            }
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                row = sheet.createRow(rowNum); // Create row if not exists
            }
            Cell cell = row.getCell(colNum);
            if (cell == null) {
                cell = row.createCell(colNum); // Create cell if not exists
            }
            cell.setCellValue(value);
            saveWorkbook(workbook, filePath);
            logger.info("setCellValue [" + sheetName + "][" + rowNum + "][" + colNum + "] = " + value);
        } catch (IOException e) {
            logger.severe("Failed to write cell value: " + e.getMessage());
        }
    }

    // ─── S3 ─────────────────────────────────────────────────────────────────

    /**
     * S3 (NEW) - Read an entire sheet as a List of row Maps
     * First row is treated as the header row (column keys)
     *
     * @param filePath  Full path to the .xlsx file
     * @param sheetName Name of the sheet
     * @return List of Maps where each Map represents a row (key = header, value = cell value)
     */
    public List<Map<String, String>> readSheet(String filePath, String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();
        try (Workbook workbook = openWorkbook(filePath)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.warning("Sheet not found: " + sheetName);
                return data;
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                logger.warning("Header row is missing in sheet: " + sheetName);
                return data;
            }

            // Collect header names
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValueAsString(cell));
            }

            // Read data rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Map<String, String> rowMap = new LinkedHashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    rowMap.put(headers.get(j), getCellValueAsString(row.getCell(j)));
                }
                data.add(rowMap);
            }
            logger.info("readSheet: " + data.size() + " data rows read from [" + sheetName + "]");
        } catch (IOException e) {
            logger.severe("Failed to read sheet: " + e.getMessage());
        }
        return data;
    }

    // ─── S4 ─────────────────────────────────────────────────────────────────

    /**
     * S4 (NEW) - Get the total number of data rows in a sheet (excludes header)
     *
     * @param filePath  Full path to the .xlsx file
     * @param sheetName Name of the sheet
     * @return Row count (0-based last row number), or -1 on failure
     */
    public int getRowCount(String filePath, String sheetName) {
        try (Workbook workbook = openWorkbook(filePath)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.warning("Sheet not found: " + sheetName);
                return -1;
            }
            int rowCount = sheet.getLastRowNum(); // 0-based; so row count = lastRowNum
            logger.info("Row count in [" + sheetName + "]: " + rowCount);
            return rowCount;
        } catch (IOException e) {
            logger.severe("Failed to get row count: " + e.getMessage());
            return -1;
        }
    }

    /**
     * S4 (NEW) - Get the total number of columns in the header row of a sheet
     *
     * @param filePath  Full path to the .xlsx file
     * @param sheetName Name of the sheet
     * @return Column count, or -1 on failure
     */
    public int getColumnCount(String filePath, String sheetName) {
        try (Workbook workbook = openWorkbook(filePath)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.warning("Sheet not found: " + sheetName);
                return -1;
            }
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) return 0;
            int colCount = headerRow.getLastCellNum(); // Returns total number of cells
            logger.info("Column count in [" + sheetName + "]: " + colCount);
            return colCount;
        } catch (IOException e) {
            logger.severe("Failed to get column count: " + e.getMessage());
            return -1;
        }
    }

    // ─── S5 ─────────────────────────────────────────────────────────────────

    /**
     * S5 (NEW) - Search for a value in a specific column and return the row index
     *
     * @param filePath    Full path to the .xlsx file
     * @param sheetName   Name of the sheet
     * @param colNum      Column index to search in (0-based)
     * @param searchValue Value to look for
     * @return Row index (0-based) of the first match, or -1 if not found
     */
    public int findRowByColumnValue(String filePath, String sheetName, int colNum, String searchValue) {
        try (Workbook workbook = openWorkbook(filePath)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.warning("Sheet not found: " + sheetName);
                return -1;
            }
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                String cellValue = getCellValueAsString(row.getCell(colNum));
                if (cellValue.equalsIgnoreCase(searchValue)) {
                    logger.info("Found '" + searchValue + "' at row index: " + i);
                    return i;
                }
            }
            logger.warning("Value '" + searchValue + "' not found in column " + colNum);
            return -1;
        } catch (IOException e) {
            logger.severe("Failed to search column: " + e.getMessage());
            return -1;
        }
    }
}