package com.expertrise.automation.utils;

import org.openqa.selenium.WebElement;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

/**
 * DateUtil - Utility class for date operations in test automation
 * Dependencies: Selenium WebDriver (for S4)
 */
public class DateUtilv1 {

    private static final Logger logger = Logger.getLogger(DateUtilv1.class.getName());

    /**
     * S1 - Return a date offset by N days from today (past or future), optionally skip weekends
     *
     * @param dateFormat      Desired output format (e.g., "MM/dd/yyyy")
     * @param days            Number of days to offset
     * @param pastOrFuture    "past" to subtract, "future" to add
     * @param includeWeekends false = skip Saturdays and Sundays
     * @return Formatted date string
     */
    public String returnDate(String dateFormat, int days, String pastOrFuture, boolean includeWeekends) {
        LocalDate date = LocalDate.now();
        int count = 0;
        int direction = pastOrFuture.equalsIgnoreCase("past") ? -1 : 1;

        while (count < days) {
            date = date.plusDays(direction);
            if (includeWeekends || (date.getDayOfWeek() != DayOfWeek.SATURDAY
                    && date.getDayOfWeek() != DayOfWeek.SUNDAY)) {
                count++;
            }
        }
        String result = date.format(DateTimeFormatter.ofPattern(dateFormat));
        logger.info("returnDate result: " + result);
        return result;
    }

    /**
     * S2 - Return a date offset by days, months, and/or years from today
     *
     * @param dateFormat      Output date format (e.g., "dd-MM-yyyy")
     * @param pastOrFuture    "past" or "future"
     * @param dayMonthYears   Offset string in format "Xd Xm Xy" (e.g., "2d 1m 0y")
     * @param includeWeekends false = skip weekends during day offset
     * @return Formatted date string
     */
    public String returnDateWithDaysMonthYears(String dateFormat, String pastOrFuture,
                                               String dayMonthYears, boolean includeWeekends) {
        int days = 0, months = 0, years = 0;

        // Parse "2d 1m 1y" format
        for (String part : dayMonthYears.trim().split("\\s+")) {
            if (part.endsWith("d")) days = Integer.parseInt(part.replace("d", ""));
            else if (part.endsWith("m")) months = Integer.parseInt(part.replace("m", ""));
            else if (part.endsWith("y")) years = Integer.parseInt(part.replace("y", ""));
        }

        int direction = pastOrFuture.equalsIgnoreCase("past") ? -1 : 1;
        LocalDate date = LocalDate.now()
                .plusMonths((long) direction * months)
                .plusYears((long) direction * years);

        // Apply day offset (with optional weekend skipping)
        int count = 0;
        while (count < days) {
            date = date.plusDays(direction);
            if (includeWeekends || (date.getDayOfWeek() != DayOfWeek.SATURDAY
                    && date.getDayOfWeek() != DayOfWeek.SUNDAY)) {
                count++;
            }
        }
        String result = date.format(DateTimeFormatter.ofPattern(dateFormat));
        logger.info("returnDateWithDaysMonthYears result: " + result);
        return result;
    }

    /**
     * S3 - Check if the given date falls on a working day (Mon-Fri)
     *
     * @param date       Date string to validate
     * @param dateFormat Format of the input date (e.g., "MM/dd/yyyy")
     * @return true if it's a weekday, false if weekend
     */
    public boolean isWorkingDay(String date, String dateFormat) {
        try {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat));
            DayOfWeek day = localDate.getDayOfWeek();
            boolean isWorking = day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
            logger.info(date + " is " + (isWorking ? "a working day" : "NOT a working day"));
            return isWorking;
        } catch (Exception e) {
            logger.severe("Invalid date or format: " + e.getMessage());
            return false;
        }
    }

    /**
     * S4 - Click the current date on a calendar web element (UI interaction)
     *
     * @param calendarElement WebElement representing the calendar container
     */
    public void clickCurrentDateOnCalendar(WebElement calendarElement) {
        String today = String.valueOf(LocalDate.now().getDayOfMonth());
        calendarElement.findElements(
                        org.openqa.selenium.By.tagName("td")
                ).stream()
                .filter(el -> el.getText().trim().equals(today))
                .findFirst()
                .ifPresent(el -> {
                    el.click();
                    logger.info("Clicked current date: " + today);
                });
    }

    /**
     * S5 - Find the number of days between two date strings
     *
     * @param startDate  Start date string
     * @param endDate    End date string
     * @param dateFormat Format of both dates (e.g., "MM/dd/yyyy")
     * @return Number of days between startDate and endDate (absolute value)
     */
    public long findDaysBetweenTwoDates(String startDate, String endDate, String dateFormat) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);
            long days = Math.abs(ChronoUnit.DAYS.between(start, end));
            logger.info("Days between " + startDate + " and " + endDate + ": " + days);
            return days;
        } catch (Exception e) {
            logger.severe("Failed to calculate days: " + e.getMessage());
            return -1;
        }
    }

    /**
     * S6 - Get a random date between a given start and end date (inclusive)
     *
     * @param startDate  Lower bound date string
     * @param endDate    Upper bound date string
     * @param dateFormat Format for both input and output (e.g., "MM/dd/yyyy")
     * @return Random date string in the same format
     */
    public String getRandomDateBetweenGivenDateRange(String startDate, String endDate, String dateFormat) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);
            long daysBetween = ChronoUnit.DAYS.between(start, end);
            long randomDays = (long) (new Random().nextDouble() * daysBetween);
            String result = start.plusDays(randomDays).format(formatter);
            logger.info("Random date between " + startDate + " and " + endDate + ": " + result);
            return result;
        } catch (Exception e) {
            logger.severe("Failed to get random date: " + e.getMessage());
            return null;
        }
    }

    /**
     * S7 - Get the current date in a specified format
     *
     * @param dateFormat Desired format (e.g., "yyyy-MM-dd", "dd/MM/yyyy")
     * @return Current date as a formatted string
     */
    public String getCurrentDate(String dateFormat) {
        String result = LocalDate.now().format(DateTimeFormatter.ofPattern(dateFormat));
        logger.info("Current date [" + dateFormat + "]: " + result);
        return result;
    }

    /**
     * S8 - Return a formatted date string from a java.util.Date object
     * Supports multiple standard formats
     *
     * @param date       java.util.Date object to format
     * @param dateFormat One of: dd-MM-YY, dd-MM-YYYY, MM-dd-YYYY, MM/dd/YYYY,
     *                   MMM/dd/YYYY, dd-MMM-YYYY, dd-MMM-YYYY hh:mm:ss, YYYY-MM-dd hh:mm:ss
     * @return Formatted date string
     */
    public String returnFormattedDate(Date date, String dateFormat) {
        try {
            // Normalize shorthand formats to Java-compatible patterns
            String javaFormat = dateFormat
                    .replace("YYYY", "yyyy")
                    .replace("YY", "yy");
            SimpleDateFormat sdf = new SimpleDateFormat(javaFormat);
            String result = sdf.format(date);
            logger.info("Formatted date: " + result);
            return result;
        } catch (Exception e) {
            logger.severe("Failed to format date: " + e.getMessage());
            return null;
        }
    }

    /**
     * S9 (NEW) - Convert a date string from one format to another
     *
     * @param date       Input date string
     * @param fromFormat Source format (e.g., "MM/dd/yyyy")
     * @param toFormat   Target format (e.g., "dd-MMM-yyyy")
     * @return Converted date string, or null on failure
     */
    public String convertDateFormat(String date, String fromFormat, String toFormat) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(fromFormat);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(toFormat);
            LocalDate parsedDate = LocalDate.parse(date, inputFormatter);
            String result = parsedDate.format(outputFormatter);
            logger.info("Converted date from [" + fromFormat + "] to [" + toFormat + "]: " + result);
            return result;
        } catch (Exception e) {
            logger.severe("Date format conversion failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * S10 (NEW) - Get the next working day (Monday-Friday) from a given date
     *
     * @param date       Input date string
     * @param dateFormat Format of input and output date
     * @return Next working day as a formatted string
     */
    public String getNextWorkingDay(String date, String dateFormat) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            LocalDate current = LocalDate.parse(date, formatter).plusDays(1);
            while (current.getDayOfWeek() == DayOfWeek.SATURDAY
                    || current.getDayOfWeek() == DayOfWeek.SUNDAY) {
                current = current.plusDays(1);
            }
            String result = current.format(formatter);
            logger.info("Next working day after " + date + ": " + result);
            return result;
        } catch (Exception e) {
            logger.severe("Failed to get next working day: " + e.getMessage());
            return null;
        }
    }

    /**
     * S11 (NEW) - Count working days (Mon-Fri) between two dates — like Excel NETWORKDAYS
     *
     * @param startDate  Start date string (inclusive)
     * @param endDate    End date string (inclusive)
     * @param dateFormat Format of both dates
     * @return Count of working days (excludes Saturdays and Sundays)
     */
    public long countWorkingDaysBetweenDates(String startDate, String endDate, String dateFormat) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);

            long workingDays = start.datesUntil(end.plusDays(1))
                    .filter(d -> d.getDayOfWeek() != DayOfWeek.SATURDAY
                            && d.getDayOfWeek() != DayOfWeek.SUNDAY)
                    .count();

            logger.info("Working days between " + startDate + " and " + endDate + ": " + workingDays);
            return workingDays;
        } catch (Exception e) {
            logger.severe("Failed to count working days: " + e.getMessage());
            return -1;
        }
    }
}