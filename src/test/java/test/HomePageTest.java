package test;

import com.zwift.elements.Home;
import com.zwift.framework.WebDriverSetup;
import com.zwift.framework.helpers.Helper;
import io.qameta.allure.*;
import org.testng.annotations.Test;

/**
 *
 */
@Epic("JIRA-1234")
@Feature("UI")
public class HomePageTest extends WebDriverSetup {

    @Test(description = "Verify Home Page")
    @Description("Home Page Loads Test")
    @Severity(SeverityLevel.CRITICAL)
    public void homePageLoads() {
        new Helper(driver).acceptCookie();
        Home home = new Home(driver);
        home.verifyPageLoads();
        home.verifyVideoDisplayed();
    }

}
