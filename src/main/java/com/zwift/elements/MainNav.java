package com.zwift.elements;

import com.zwift.framework.helpers.Helper;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by DHOLMAN on 09/13/21.
 */
public class MainNav {

    private WebDriver driver;
    private Helper helper;

    private By bHamburger = By.cssSelector("button[class^='PrimaryNav-module__hamburger']");
    private By mEvents = By.linkText("Events");

    public MainNav(WebDriver driver) {
        this.driver = driver;
        helper = new Helper(driver);
    }

    @Step("Click Main Menu Button")
    public void clickMenuButton() {
        helper.click("Main Menu Hamburger", bHamburger);
    }

    @Step("Click Events")
    public void clickEventsLink() {
        helper.click("Events Link", mEvents);
    }

}
