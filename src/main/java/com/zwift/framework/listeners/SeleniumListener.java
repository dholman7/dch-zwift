package com.zwift.framework.listeners;

import com.zwift.framework.WebDriverSetup;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Listener used in Selenium tests to log info about them and take screenshots of failures.
 */
public class SeleniumListener extends TestListenerAdapter implements ISuiteListener {


    @Override
    public void onTestFailure(ITestResult tr) {

        super.onTestFailure(tr);

        try {
            if (tr.getInstance() instanceof WebDriverSetup) {

                WebDriverSetup setup = (WebDriverSetup) tr.getInstance();

                try {

                    WebDriver driver = setup.getDriver();

                    makeScreenshotOnFailure(driver);

                    LogEntries entries = driver.manage().logs().get(LogType.BROWSER);

                    System.out.println("======== CHROME LOGS ========");
                    for (LogEntry entry : entries) {
                        Reporter.log(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage(), true);
                    }
                    System.out.println("=============================");

                    File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    String destDir = ("screenshots");
                    new File(destDir).mkdirs();
                    String destFile = tr.getName() + "_" + tr.getEndMillis() + ".png";

                    try {
                        FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Reporter.setEscapeHtml(false);
                    Reporter.log("Saved a screenshot for: " + tr.getName() + "\n<img class=\"responsive-img materialboxed\" src=./screenshots/" + destFile + " width=\"1280\" height=\"720\" >", true);

                } catch (Exception e) {
                    Reporter.log("Could not take Screenshot (non WebDriver class...?)", true);
                }
            }

        } catch (Exception e) {
            Reporter.log("Could not post Sauce status: " + e.getMessage(), true);
        }

    }

    @Override
    public void onTestSuccess(ITestResult tr) {
    }

    private byte[] makeScreenshotOnFailure(WebDriver driver) {

        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Override
    public void onStart(ISuite suite) {

    }

    @Override
    public void onFinish(ISuite suite) {

    }
}