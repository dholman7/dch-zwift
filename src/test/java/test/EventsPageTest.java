package test;

import com.zwift.elements.Events;
import com.zwift.elements.FilterEventsMenu;
import com.zwift.elements.MainNav;
import com.zwift.framework.WebDriverSetup;
import com.zwift.framework.helpers.Helper;
import io.qameta.allure.*;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

/**
 *
 */
@Epic("JIRA-4567")
@Story("As an end user, I should be able to search for events using filters so I can see if an event exists.")
@Feature("UI")
public class EventsPageTest extends WebDriverSetup {

    @Test(description = "Filtering Events Change")
    @Severity(SeverityLevel.NORMAL)
    @Description("Filtering for Events Changes Results")
    public void userFilterForEvent() {
        new Helper(driver).acceptCookie();
        MainNav mainNav = new MainNav(driver);
        mainNav.clickMenuButton();
        mainNav.clickEventsLink();
        Events events = new Events(driver);
        events.verifyPageLoads();
        List<WebElement> initialEvents = events.getEvents();
        events.clickFilterEventsButton();
        FilterEventsMenu filterEventsMenu = new FilterEventsMenu(driver);
        filterEventsMenu.clickCycling();
        filterEventsMenu.clickGroupRide();
        filterEventsMenu.clickNight();
        filterEventsMenu.clickApplyFilters();
        List<WebElement> filteredEvents = events.getEvents();
        events.verifyEventChanged(initialEvents, filteredEvents);
    }

}
