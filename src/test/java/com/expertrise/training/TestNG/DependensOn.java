package com.expertrise.training.TestNG;

import org.testng.annotations.Test;

public class DependensOn {
    @Test
    public void testMethodA() {
        System.out.println("Executing Test Method A");

    }

    @Test(dependsOnMethods = {"testMethodA"})
    public void testMethodB() {
        System.out.println("Executing Test Method B, depends on A");
    }

    @Test(dependsOnMethods = {"testMethodB"})
    public void testMethodC() {
        System.out.println("Executing Test Method C, depends on B");
    }
}
