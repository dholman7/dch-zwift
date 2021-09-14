package com.zwift.framework;

import com.zwift.framework.listeners.AllureListener;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import static org.testng.Reporter.log;

/**
 * Created by DHOLMAN on 09/13/21.
 * Base Class used to set properties for all tests.
 * Listener added here as workaround for an Allure Bug
 */
@Listeners({AllureListener.class})
public class Base {

    /**
     * Static test properties
     */
    private static final Properties properties = new Properties();

    /**
     * Non-static test properties
     */
    private ZonedDateTime dateTime;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yy HH:mm:ss.SSS");

    /**
     * Gets a property from the static property store, based on the key passed in
     *
     * @param key The key of the property you want
     * @return Returns a value for the given key
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Sets up static test properties
     */
    @BeforeSuite(alwaysRun = true)
    @Parameters({"environment"})
    public void setupStaticProperties(ITestContext context, String environment) {

        try {

            if (environment.equalsIgnoreCase("STAGE")) {

                //<editor-fold desc="">
                properties.setProperty("ENV", "STAGE");
                properties.setProperty("URL", "https://stage.zwift.com");
                properties.setProperty("API_URL", "https://stage-api.zwift.com");
                //</editor-fold>

            } else if (environment.equalsIgnoreCase("PROD")) {

                //<editor-fold desc="">
                properties.setProperty("ENV", "PROD");
                properties.setProperty("URL", "https://zwift.com");
                properties.setProperty("API_URL", "https://api.zwift.com");
                //</editor-fold>
            }

        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    @BeforeMethod
    public void logTime(ITestResult result) {
        dateTime = LocalDateTime.now().atZone(ZoneId.of("America/Los_Angeles"));
        log("Test Started at: " + dateTime.format(formatter), true);
    }

    /**
     * Adds a timestamp session has exited.
     *
     * @param result An object reference to the results of the ran test.
     */
    @AfterMethod
    public void breakDown(ITestResult result) {
        dateTime = LocalDateTime.now().atZone(ZoneId.of("America/Los_Angeles"));
        log("Test Ended at: " + dateTime.format(formatter), true);
    }
}
