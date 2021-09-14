package com.zwift.framework.helpers;

import com.zwift.framework.WebDriverSetup;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertTrue;

/**
 * This class is a collection of helper methods geared towards manipulated and extracting information from the DOM
 * <p>
 * Created by DHOLMAN on 09/13/21.
 */
public class Helper {

    // Sets the default timeout and polling rate of helper methods
    private static final long DEFAULT_TIMEOUT = 10;
    private static final long DEFAULT_POLL = 200;

    public WebDriver driver;

    /**
     * @param driver The WebDriver object created in {@link WebDriverSetup
     *               WebDriverSetup}
     */
    public Helper(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Universal click method
     * <p>
     * Click element for given By selector
     *
     * @param elementName String name of the element for reporting purposes
     * @param selector    Selector for the element
     */
    public void click(String elementName, By selector) {
        Reporter.log("Clicking " + elementName, true);
        try {
            WebElement element = waitClickable(selector);
            element.click();
        } catch (TimeoutException e) {
            throw new NoSuchElementException("Unable to locate element: " + elementName + " Button (" + selector + ")" + e);
        }
        Reporter.log("Clicked " + elementName, true);
    }

    /**
     * Universal click method using JSE - Needed for React
     * <p>
     * Click element for given By selector
     *
     * @param elementName String name of the element for reporting purposes
     * @param selector    Selector for the element
     */
    public void clickJS(String elementName, By selector) {
        Reporter.log("Clicking " + elementName + " ...", true);
        try {
            WebElement element = waitPresent(selector);
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            jse.executeScript("arguments[0].click();", element);
        } catch (TimeoutException e) {
            throw new NoSuchElementException("Unable to locate element: " + elementName + " Button (" + selector + ")" + e);
        }
        Reporter.log("Clicked " + elementName, true);
    }

    /**
     * Verify displayed for selector
     *
     * @param elementName     String name of element for reporting purposes
     * @param elementSelector selector for element
     */
    public void verifyDisplayed(String elementName, By elementSelector) {
        try {
            assertTrue(waitVisible(elementSelector).isDisplayed(), elementName + " is displayed: ");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Unable to locate element: " + elementName + " (" + elementSelector + ")");
        }
        Reporter.log(elementName + " displayed", true);
    }

    /**
     * Fluent wait for element to be clickable
     * <p>
     * Return a clickable WebElement
     *
     * @param selector for the element
     * @return desired WebElement
     * @throws Exception
     */
    public WebElement waitClickable(By selector) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(ExpectedConditions.elementToBeClickable(selector));
    }

    /**
     * Fluent wait for element to be clickable before clicking it
     *
     * @param element The element you want to click
     * @throws Exception
     */
    public void waitClick(WebElement element) {
        new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .ignoring(NoSuchElementException.class)
                .until(ExpectedConditions.elementToBeClickable(element))
                .click();
    }

    /**
     * Fluent wait till element is visible
     * <p>
     * Return visible WebElement
     *
     * @param selector for the element
     * @return Visible WebElement
     */
    public WebElement waitVisible(By selector) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOfElementLocated(selector));
    }

    /**
     * Fluent wait for element to be present in the dom
     * <p>
     * Return present WebElement
     *
     * @param selector for the element
     * @return Present WebElement
     */
    public WebElement waitPresent(By selector) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(ExpectedConditions.presenceOfElementLocated(selector));
    }

    /**
     * Fluent wait for visibility of all elements located by the selector
     * <p>
     * Return list of WebElements for selector
     *
     * @param selector of the desired elements
     * @return List of WebElements for selector
     */
    public List<WebElement> waitListVisible(By selector) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(selector));
    }

    /**
     * Waits for an Cookies dialog to pop up on screen and accepts.
     *
     */
    public void acceptCookie() {
        waitClick(driver.findElement(By.id("truste-consent-button")));
    }
}
