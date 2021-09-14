package com.zwift.elements;

import com.zwift.framework.helpers.Helper;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by DHOLMAN on 09/13/21.
 */
public class Events {

    private WebDriver driver;
    private Helper helper;

    private By bFiterToggle = By.cssSelector(".filter-toggle");
    private By dEvents = By.cssSelector(".tab-listing");

    public Events(WebDriver driver) {
        this.driver = driver;
        helper = new Helper(driver);
    }

    @Step("Click Filter Events Button")
    public void clickFilterEventsButton() {
        helper.click("Filter Events", bFiterToggle);
    }

    @Step("Get the displayed events")
    public List<WebElement> getEvents() {
        List<WebElement> list = helper.waitListVisible(dEvents);
        return list;
    }

    @Step("Verify Filtering Changed Events")
    public void verifyEventChanged(List<WebElement> initialEvents, List<WebElement> filteredEvents) {
        Assert.assertFalse(initialEvents == filteredEvents, "Initial Events Matched Filtered Events");
    }

    @Step("Validate the page loads")
    public void verifyPageLoads() {
        Assert.assertTrue(driver.getCurrentUrl().contains("zwift.com/events"), "Actual URL: " + driver.getCurrentUrl());
        Assert.assertTrue(driver.getTitle().contains("The at Home Cycling & Running Virtual Training App"), "Actual: " + driver.getTitle());
    }

}
