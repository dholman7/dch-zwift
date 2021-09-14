package com.zwift.framework.listeners;

import com.zwift.framework.Base;
import com.zwift.framework.WebDriverSetup;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.util.List;

/**
 * Requires JVM parameter -javaagent:"lib/aspectjweaver-1.9.6.jar"
 */
public class AllureListener extends Base implements ITestListener {
    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    //PNG attachments for Allure
    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] saveScreenshotPNG(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    //Text attachments for Allure
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        Reporter.log("onStart method " + iTestContext.getName(), true);
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        Reporter.log("onFinish method " + iTestContext.getName(), true);
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        Reporter.log(getTestMethodName(iTestResult) + " test starting.", true);
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        Reporter.log(getTestMethodName(iTestResult) + " test succeeded.", true);

        //Save a log on allure.
        logOutput(Reporter.getOutput(iTestResult));
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        Reporter.log(getTestMethodName(iTestResult) + " test failed.", true);

        if (iTestResult.getInstance() instanceof WebDriverSetup) {
            WebDriverSetup setup = (WebDriverSetup) iTestResult.getInstance();
            WebDriver driver = setup.getDriver();

            // Allure ScreenShot
            if (driver != null) {
                //allureChromeLog(driver);
                saveScreenshotPNG(driver);
                Reporter.log("Screenshot captured for test case:" + getTestMethodName(iTestResult), true);
            }
        }

        //Save a log on allure.
        logOutput(Reporter.getOutput(iTestResult));

    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        Reporter.log(getTestMethodName(iTestResult) + " test skipped.", true);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        Reporter.log("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult), true);
    }

    @Attachment()
    public String logOutput(List<String> outputList) {
        String output = "";
        for (String o : outputList)
            output += o + "\n";
        return output;
    }

}