package com.expertrise.training.TestNG;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Groups {
    @BeforeClass
    public void beforeClass() {
        System.out.println("This method runs before class");
    }
    @BeforeMethod
    public void beforeMethod() {
        System.out.println();
        System.out.println("This method runs before every method");
    }

    @Test(groups = { "Sedan", "BMW" })
    public void BMW3Series() {
        System.out.println("Running Test - BMW 3 Series");
    }
    @Test(groups = { "SUV", "BMW" })
    public void BMWX3() {
        System.out.println("Running Test - BMW X3");
    }
    @Test(groups = { "Sedan", "Audi" })
    public void AudiA6() {
        System.out.println("Running Test - Audi A6");
    }
    @Test(groups = { "Sedan", "Honda" })
    public void HondaAccord() {
        System.out.println("Running Test - Honda Accord");
    }
    @Test(groups = {"smoke", "loginTests"})
    public void verifyValidLogin() { System.out.println("smoke Login Test"); }

    @Test(groups = "regression")
    public void verifyCheckout() {System.out.println("Regression Login Test") ;}


    @AfterMethod
    public void afterMethod() {
        System.out.println("This method runs after every method");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("This method runs after class");
    }

}
