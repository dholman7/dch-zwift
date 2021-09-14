package com.zwift.elements;

import com.zwift.framework.helpers.Helper;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by DHOLMAN on 09/13/21.
 * Suggested Selenium Selector by Performance
 * [id, name, linkText, partialLinkText, tagName, className, cssSelector, xpath]
 */
public class FilterEventsMenu {

    private WebDriver driver;
    private Helper helper;

    private By bApplyFilters = By.cssSelector(".apply-button");
    private By bClose = By.linkText("Close");
    private By bCycling = By.cssSelector("button[value='CYCLING']");
    private By bGroupRide = By.cssSelector("button[value='GROUP_RIDE']");
    private By bNight = By.cssSelector("button[value='night']");


    public FilterEventsMenu(WebDriver driver) {
        this.driver = driver;
        helper = new Helper(driver);
    }

    @Step("Click Apply Filters")
    public void clickApplyFilters() {
        helper.clickJS("Apply Filters", bApplyFilters);
    }

    @Step("Click Close")
    public void clickClose() {
        helper.clickJS("Close", bClose);
    }

    @Step("Click Cycling")
    public void clickCycling() {
        helper.clickJS("Cycling", bCycling);
    }

    @Step("Click E(Open)")
    public void clickNight() {
        helper.clickJS("Night", bNight);
    }

    @Step("Click Group Ride")
    public void clickGroupRide() {
        helper.clickJS("Group Ride", bGroupRide);
    }


}
