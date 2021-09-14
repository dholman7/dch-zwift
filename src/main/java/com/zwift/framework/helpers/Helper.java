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
     * Universal click method
     * <p>
     * Click element for given WebElement
     *
     * @param buttonName name of button
     * @param element    corresponding to button
     */
    public void click(String buttonName, WebElement element) {
        try {
            waitClick(element);
        } catch (TimeoutException e) {
            throw new NoSuchElementException("Unable to locate element: " + buttonName + " Button (" + element + ")" + e);
        }
        Reporter.log("Clicked " + buttonName, true);
    }

    /**
     * Universal click method using JSE - Needed for ReactJS
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
     * Input given text
     *
     * @param inputName String name of input element
     * @param input     String text to be input
     * @param selector  for input element
     */
    public void input(String inputName, String input, By selector) {
        try {
            WebElement element = waitVisible(selector);
            element.clear(); //does not work with React on some fields
            element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE)); // TODO: 9/10/19 DHOLMAN Does not work with IE on EOW Pass
            element.sendKeys(input);
            Reporter.log("Input " + inputName + ": " + input, true);
        } catch (TimeoutException e) {
            throw new NoSuchElementException("Unable to locate element: " + inputName + " (" + input + ")" + e);
        }
    }

    /**
     * Input given text
     *
     * @param inputName  String name of input element
     * @param input      String text to be input
     * @param webElement for input WebElement
     */
    public void input(String inputName, String input, WebElement webElement) {
        try {
            waitVisible(webElement);
            webElement.clear();
            webElement.sendKeys(input);
            Reporter.log("Input " + inputName + ": " + input, true);
        } catch (TimeoutException e) {
            throw new NoSuchElementException("Unable to locate element: " + inputName + " (" + input + ")" + e);
        }
    }

    /**
     * Input given text and then hit Enter
     *
     * @param inputName     String name of input element
     * @param input         String text to be input
     * @param inputSelector for input element
     */
    public void inputAndEnter(String inputName, String input, By inputSelector) {
        try {
            WebElement element = waitVisible(inputSelector);
            element.clear();
            element.sendKeys(input);
            element.sendKeys(Keys.ENTER);
            Reporter.log("Input " + inputName + ": " + input, true);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Unable to locate element: " + inputName + " (" + input + ")");
        }
    }

    /**
     * Input given text
     *
     * @param inputName String name of input element
     * @param input     String text to be input
     * @param selector  for input element
     */
    public void inputPresent(String inputName, String input, By selector) {
        try {
            WebElement element = waitPresent(selector);
            element.clear(); //does not work with React on some fields
            element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
            element.sendKeys(input);
            Reporter.log("Input " + inputName + ": " + input, true);
        } catch (TimeoutException e) {
            throw new NoSuchElementException("Unable to locate element: " + inputName + " (" + input + ")" + e);
        }
    }

    /**
     * Move to Elements found by selector
     *
     * @param selector for element to be moved to
     * @throws Exception
     */
    public void moveTo(By selector) {
        new Actions(driver).moveToElement(waitVisible(selector)).perform();
    }

    /**
     * Move to WebElement
     *
     * @param webElement of element to be moved to
     * @throws Exception
     */
    public void moveTo(WebElement webElement) {
        new Actions(driver).moveToElement(webElement).perform();
    }

    /**
     * Get an attribute of an element
     *
     * @param elementSelector The selector for the element
     * @param attributeName   The attribute you want
     * @return Returns a String value of the request element attribute
     */
    public String getAttribute(By elementSelector, String attributeName) {
        moveTo(elementSelector);
        return waitVisible(elementSelector).getAttribute(attributeName);
    }

    /**
     * Refreshes the page
     */
    public void refreshPage() {
        driver.navigate().refresh();
    }

    /**
     * Return list of row elements from table selector
     *
     * @param selector for the table element
     * @return List of row WebElements
     */
    public List<WebElement> getRows(By selector) {
        return waitNestedListVisible(selector, By.tagName("tr"));
    }

    /**
     * Return list of column elements from table selector
     *
     * @param selector for the table element
     * @return List of column WebElements
     */
    public List<WebElement> getColumns(By selector) {
        return waitNestedListVisible(selector, By.tagName("td"));
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
     * Verify element contains given text
     *
     * @param elementName String name of element for reporting purposes
     * @param selector    selector for element
     * @param contains    text to verify
     */
    public void verifyContains(String elementName, By selector, String contains) {
        verifyDisplayed(elementName, selector);
        WebElement element = waitVisible(selector);
        assertTrue(element.getText().contains(contains), elementName + " contains: " + contains + ",");
        Reporter.log(elementName + " contains: " + contains, true);
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
     * Fluent wait for element to be clickable
     * <p>
     * Return a clickable WebElement
     *
     * @param selector Webelememnt
     * @return desired WebElement
     * @throws Exception
     */
    public WebElement waitClickable(WebElement selector) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(ExpectedConditions.elementToBeClickable(selector));
    }

    /**
     * Same functionality of {@link #waitClickable(By) waitClickable}, but with a definable timeout.
     *
     * @param selector The element you want to click
     * @param timeout  How long to wait for the element to be clickable in seconds
     * @return Returns a WebElement that is clickable
     * @throws Exception
     */
    public WebElement waitClickable(By selector, long timeout) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(ExpectedConditions.elementToBeClickable(selector));
    }

    /**
     * Same functionality of {@link #waitClickable(By) waitClickable}, but with a definable timeout.
     *
     * @param selector The element you want to click
     * @param timeout  How long to wait for the element to be clickable in seconds
     * @param poll     How often to poll the element to be clickable in milliseconds
     * @return Returns a WebElement that is clickable
     * @throws Exception
     */
    public WebElement waitClickable(By selector, long timeout, long poll) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofMillis(poll))
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
     * Fluent wait till element is visible
     * <p>
     * Return visible WebElement
     *
     * @param element for the element
     * @return Visible WebElement
     */
    public WebElement waitVisible(WebElement element) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOf(element));
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
     * Fluent wait for nested element to be present in the dom
     *
     * @param parentSelector for the parent element
     * @param childSelector  for the child element
     * @return
     */
    public WebElement waitNestedPresent(By parentSelector, By childSelector) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(ExpectedConditions.presenceOfNestedElementLocatedBy(parentSelector, childSelector));
    }

    /**
     * Fluent wait for nested element to be present in the dom
     *
     * @param parentWebElement for the parent element
     * @param childSelector    for the child element
     * @return
     */
    public WebElement waitNestedPresent(WebElement parentWebElement, By childSelector) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(ExpectedConditions.presenceOfNestedElementLocatedBy(parentWebElement, childSelector));
    }

    /**
     * Fluent wait till text present in element
     * <p>
     * Return element with text present
     *
     * @param selector for the element
     * @param text     to be present
     * @return Visible WebElement
     */
    public void waitTextPresent(By selector, String text) {
        assertTrue(new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(ExpectedConditions.textToBePresentInElementLocated(selector, text)), text + " present in element: " + selector);
    }

    /**
     * Waits for an element to be present in the DOM, and then returns all matching elements in a List
     *
     * @param selector The element you're looking for
     * @return Returns a List of WebElements matching the selector
     */
    public List<WebElement> waitListPresent(By selector) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(selector));
    }

    /**
     * Waits for an element to be present in the DOM, and then returns all matching elements in a List
     *
     * @param selector The element you're looking for
     * @param seconds  The seconds you wish to wait
     * @return Returns a List of WebElements matching the selector
     */
    public List<WebElement> waitListPresent(By selector, int seconds) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(seconds))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(selector));
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
     * Fluent wait for visibility of all elements located by the selector
     * <p>
     * Return list of WebElements for selector
     *
     * @param selector of the desired elements
     * @return List of WebElements for selector
     */
    public List<WebElement> waitListVisible(By selector, int timeout) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(selector));
    }

    /**
     * Fluent wait for visibility of all elements located by childSelector nested in the parent selector
     *
     * @param parentSelector for the parent element
     * @param childSelector  for the nested element
     * @return Returns a List of WebElements
     */
    public List<WebElement> waitNestedListVisible(By parentSelector, By childSelector) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(parentSelector, childSelector));
    }

    /**
     * Fluent wait for visibility of all elements located by childSelector nested in the parent selector
     *
     * @param parentSelector for the parent element
     * @param childSelector  for the nested element
     * @return Returns a List of WebElements
     */
    public List<WebElement> waitNestedListVisible(By parentSelector, By childSelector, Long poll) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(poll))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(parentSelector, childSelector));
    }

    /**
     * Fluent wait for visibility of all elements located by childSelector nested in the parent selector
     *
     * @param parentWebElement for the parent element
     * @param childSelector    for the nested element
     * @return Returns a List of WebElements
     */
    public List<WebElement> waitNestedListVisible(WebElement parentWebElement, By childSelector) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(parentWebElement, childSelector));
    }

    /**
     * Fluent wait for visibility of all elements located by childSelector nested in the parent selector
     *
     * @param parentWebElement for the parent element
     * @param childSelector    for the nested element
     * @return Returns a List of WebElements
     */
    public List<WebElement> waitNestedListVisible(WebElement parentWebElement, By childSelector, Long poll) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(poll))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(parentWebElement, childSelector));
    }

    /**
     * Fluent wait for all elements located by childSelector to be clickable nested in the parent selector
     *
     * @param parentWebElement for the parent element
     * @param childSelector    for the nested element
     * @return Returns a List of WebElements
     */
    public List<WebElement> waitNestedListClickable(WebElement parentWebElement, By childSelector) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(new ExpectedCondition<List<WebElement>>() {
                           @Override
                           public List<WebElement> apply(WebDriver webDriver) {
                               List<WebElement> allChildren = parentWebElement.findElements(childSelector);
                               return !allChildren.isEmpty() && allChildren.stream().allMatch(WebElement::isDisplayed) && allChildren.stream().allMatch(WebElement::isEnabled) ? allChildren : null;
                           }

                           @Override
                           public String toString() {
                               return String.format("element to be clickable located by %s -> %s", parentWebElement, childSelector);
                           }
                       }
                );
    }

    /**
     * Fluent wait for element to not be displayed on the page or present in the dom
     *
     * @param selector for the element
     */
    public void waitInvisible(By selector) {
        assertTrue(new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .until(ExpectedConditions.invisibilityOfElementLocated(selector)), "Element: (" + selector + ") is not visible");
    }

    /**
     * Fluent wait for element to not be displayed on the page or present in the dom
     *
     * @param selector for the element
     * @param seconds  the number of seconds to wait
     */
    public void waitInvisible(By selector, int seconds) {
        assertTrue(new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(seconds))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .until(ExpectedConditions.invisibilityOfElementLocated(selector)), "Element: (" + selector + ") is not visible");
    }

    /**
     * Fluent wait for element to not be displayed on the page or present in the dom
     *
     * @param element WebElement to check for
     * @throws Exception
     */
    public void waitInvisible(WebElement element) {
        assertTrue(new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .until(ExpectedConditions.invisibilityOf(element)), element + " is not visible");
    }

    /**
     * Fluent wait for element (input) to be populated with something
     *
     * @param selector
     * @return
     */
    public WebElement waitInputPopulated(By selector) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(new ExpectedCondition<WebElement>() {
                    @Override
                    public WebElement apply(WebDriver input) {
                        return !input.findElement(selector).getAttribute("value").isEmpty() ? input.findElement(selector) : null;
                    }

                    @Override
                    public String toString() {
                        return "input element to be populated: " + selector;
                    }
                });
    }

    /**
     * Fluent wait for certain element count
     *
     * @param selector for element
     * @param number   of elements expected
     */
    public void waitNumberOfElements(By selector, int number) {
        new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofMillis(DEFAULT_POLL))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(ExpectedConditions.numberOfElementsToBe(selector, number));
    }

    /**
     * Waits for an Alert dialog to pop on screen.
     *
     * @return Returns an Alert that has been waited for.
     */
    public Alert waitForAlert() {
        return new WebDriverWait(driver, DEFAULT_TIMEOUT).until(ExpectedConditions.alertIsPresent());
    }

    /**
     * Waits for the specified attribute to be a specific value. This is useful for elements that load before populating
     * the data. (ReactJS)
     *
     * @param by            Selenium By
     * @param attribute     The CSS Attribute
     * @param expectedValue The value you expect the attribute to contain
     */
    public void waitForAttribute(By by, String attribute, String expectedValue) {
        new WebDriverWait(driver, 20).until(ExpectedConditions.attributeToBe(driver.findElement(by), attribute, expectedValue));
    }

    /**
     * Waits for the textContent attribute to be a specific value. This is also useful for elements that .getText()
     * doesn't work on.
     *
     * @param by            Selenium By
     * @param expectedValue The value you expect the attribute to contain
     */
    public void waitForTextContent(By by, String expectedValue) {
        new WebDriverWait(driver, 20).until(ExpectedConditions.attributeToBe(driver.findElement(by), "textContent", expectedValue));
    }

    /**
     * Waits for the textContent attribute contain a specific value. This is also useful for elements that .getText()
     * doesn't work on.
     *
     * @param by            Selenium By
     * @param expectedValue The value you expect the attribute to contain
     */
    public void verifyTextContent(By by, String expectedValue) {
        new WebDriverWait(driver, 20).until(ExpectedConditions.attributeContains(driver.findElement(by), "textContent", expectedValue));
    }

    /**
     * Gets the textContent attribute
     *
     * @param by Selenium By
     */
    public String getTextContent(By by) {
        String text = getAttribute(by, "textContent");
        Reporter.log("Found textContent: " + text, true);
        return text;
    }

    /**
     * @param textContent
     * @return
     */
    public WebElement findElementByTextContent(String textContent) {
        By by = By.xpath("//*[text() = '" + textContent + "']");
        return waitVisible(by);
    }

    public void clear(By selector) {
        click("TextBox", selector);
        waitVisible(selector).clear();
        waitVisible(selector).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
    }

    /**
     * Waits for an Cookies dialog to pop up on screen and accepts.
     *
     */
    public void acceptCookie() {
        waitClick(driver.findElement(By.id("truste-consent-button")));
    }
}
