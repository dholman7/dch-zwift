package com.zwift.framework;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Base class that will run with all WebDriver tests.
 * Browser and URL are set by the testng.xml file (test.suite.zwift-regression.xml).
 * If environment is set to `LOCAL` - local drivers will be used and no Selenium Grid is needed.
 */
public class WebDriverSetup extends Base {

    public WebDriver driver;

    private String baseURL;
    private String headless;

    private static final String MAC_DRIVER = "/chromedriver";
    private static final String WINDOWS_DRIVER = "/chromedriver.exe";

    private static final String WINDOW_RESOLUTION = "1920,1080";

    public WebDriver getDriver() {
        return driver;
    }

    /**
     * This will run before every Method in a test case that extends WebDriverSetup. The parameters are taken from the testng.xml file that is
     * executed. Environment "LOCAL" will use a local driver.
     *
     * @param environment STAGE or PROD
     * @param browser     CHROME, SAFARI, EDGE, FIREFOX (CHROME ONLY IMPLEMENTED IN THIS DEMO)
     * @param config      .properties file for passing test parameters
     * @param headless    TRUE or FALSE - will run browser in headless mode
     */
    @BeforeMethod
    @Parameters({"environment", "browser", "config", "headless"})
    public void openRemoteBrowser(String environment, String browser, String config, String headless) throws Exception {

        this.headless = headless;

        baseURL = Base.getProperty("URL");

        Reporter.log("Test Executed: " + environment, true);

        if (browser.equalsIgnoreCase("chrome")) {
            chrome();
        } else {
            Reporter.log("TestNG parameter \"browser\" must be CHROME in this demo", true);
        }
        TimeUnit.MILLISECONDS.sleep(500);
    }

    /**
     * Sets up the Chrome Driver.
     *
     * @throws URISyntaxException Bad String or path to a file.
     */
    private void chrome() throws URISyntaxException {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("chrome.switches", "--disable--extensions");
        options.addArguments("window.size", WINDOW_RESOLUTION);
        options.addArguments("--disable-infobars");

        // Gets browser console and network logs - does not work with ChromeOptions
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.SEVERE);
        logPrefs.enable(LogType.BROWSER, Level.SEVERE);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

        if (headless.equalsIgnoreCase("TRUE")) {
            options.addArguments("--headless");
        }

        if (System.getProperty("os.name").contains("Mac")) {
            File cDriver = new File(this.getClass().getResource(MAC_DRIVER).toURI());
            if (!cDriver.canExecute()) {
                cDriver.setExecutable(true);
            }
            System.setProperty("webdriver.chrome.driver", this.getClass().getResource(MAC_DRIVER).toURI().getPath());
        } else {
            File wDriver = new File(this.getClass().getResource(WINDOWS_DRIVER).toURI());
            if (!wDriver.canExecute()) {
                wDriver.setExecutable(true);
            }
            System.setProperty("webdriver.chrome.driver", this.getClass().getResource(WINDOWS_DRIVER).toURI().getPath());
        }
        driver = new ChromeDriver(options);
        navigate(baseURL);
    }

    /**
     * Cleans up after a Selenium session has exited.
     *
     * @param result An object reference to the results of the ran test.
     */
    @AfterMethod
    public void breakDown(ITestResult result) {
        if (driver != null) {
            try {
                driver.quit();
            } catch (WebDriverException e) {
                Reporter.log("***** CAUGHT EXCEPTION IN DRIVER TEARDOWN *****", true);
                e.printStackTrace();
            }
            driver.quit();
        }
    }

    /**
     * Wrapper method for {@link WebDriver.Navigation#to(String) driver.navigate.to}
     * <p>
     * Also automatically logs the URL passed in to the logs and stdout
     *
     * @param url The URL you want to navigate to
     */
    @Step("Navigate to {url}")
    public void navigate(String url) {
        Reporter.log("Navigating to: " + url, true);
        driver.navigate().to(url);
    }

}