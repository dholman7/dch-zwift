package com.zwift.elements;

import com.zwift.framework.helpers.Helper;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

/**
 * Created by DHOLMAN on 09/13/21.
 */
public class Home {

    private WebDriver driver;
    private final Helper helper;

    private By vHeroVid = By.cssSelector("video[class^='Hero-module__video']");

    public Home(WebDriver driver) {
        this.driver = driver;
        helper = new Helper(driver);
    }

    @Step("Validate the page loads")
    public void verifyPageLoads() {
        Assert.assertTrue(driver.getCurrentUrl().contains("zwift.com"), "Actual: " + driver.getCurrentUrl());
        Assert.assertTrue(driver.getTitle().contains("The at Home Cycling & Running Virtual Training App - Zwift"), "Actual: " + driver.getTitle());
    }

    @Step("Validate content of your choice is present")
    public void verifyVideoDisplayed() {
        helper.verifyDisplayed("Hero Video", vHeroVid);
    }

}
