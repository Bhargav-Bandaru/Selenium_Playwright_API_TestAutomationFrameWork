package com.expertrise.training.TestNG;

import org.testng.annotations.*;

public class Annotations {

 /*
 >>> @BeforeSuite
>>> @BeforeTest
>>> @BeforeClass

>>> @BeforeMethod
>>> @Test: Executing Test Method A
>>> @AfterMethod

>>> @BeforeMethod
>>> @Test: Executing Test Method B
>>> @AfterMethod

>>> @AfterClass
>>> @AfterTest
>>> @AfterSuite
 */





    // Runs once before the entire suite
    @BeforeSuite
    public void beforeSuite() {
        System.out.println(">>> @BeforeSuite: Global setup (DB, config, reporting tools)");
    }

    // Runs before each <test> tag in testng.xml
    @BeforeTest
    public void beforeTest() {
        System.out.println(">>> @BeforeTest: (e.g., launch browser)");
    }

    // Runs once per class
    @BeforeClass
    public void beforeClass() {
        System.out.println(">>> @BeforeClass: Initialize WebDriver, Page Objects");
    }

    // Runs before EACH @Test method
    @BeforeMethod
    public void beforeMethod() {
        System.out.println(">>> @BeforeMethod: Prepare state before test (navigate, reset)");
    }

    // Actual test logic
    @Test
    public void testMethodA() {
        System.out.println(">>> @Test: Executing Test Method A e.g  validate Login Functionality" );
    }

    @Test
    public void testMethodB() {
        System.out.println(">>> @Test: Executing Test Method B e.g validate Home Page links" );
    }

    // Runs after EACH @Test method
    @AfterMethod
    public void afterMethod() {
        System.out.println(">>> @AfterMethod: Cleanup after test (logout, clear cookies)");
    }

    // Runs once per class after all tests
    @AfterClass
    public void afterClass() {
        System.out.println(">>> @AfterClass: Close browser, release resources");
    }

    // Runs after each <test> tag in testng.xml
    @AfterTest
    public void afterTest() {
        System.out.println(">>> @AfterTest: Reset environment, clear test data");
    }

    // Runs once after the entire suite
    @AfterSuite
    public void afterSuite() {
        System.out.println(">>> @AfterSuite: Generate report, email results, close DB");
    }
}
