package com.expertrise.training.TestNG;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DependsOnGroups {

    @Test(groups = "loginTests")
    public void verifyValidLogin() {
        Assert.assertTrue(true);
    }

    @Test(groups = "loginTests")
    public void verifyInvalidLogin() {
        Assert.assertTrue(true);
    }

    // Runs only if ALL loginTests pass
    @Test(dependsOnGroups = "loginTests")
    public void addToCart() {
        System.out.println("All login tests passed");
    }

}
