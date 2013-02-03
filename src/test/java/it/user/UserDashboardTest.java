package it.user;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Test;

@Setup({ "../deleteAll.xml", "UserDashboardTest.xml" })
@Verify
@BaseUrl("http://localhost:8080/signboard")
public class UserDashboardTest extends ITBase {

	@Test
	public void fn() {
		login("email02", "qqqq");
		verifyTitle("My dashboard");
		assertTextPresent("Dashboard of firstName02 lastName02");
		assertTextPresent("email02");
		assertTextPresent("phone02");
		assertTextPresent("S skype02");
		assertElementPresentByXPath("//div[@id='main-section']//i[@class='icon-phone']");
	}

	@Test
	public void fn_OptionalFields() {
		login("email03", "qqqq");
		verifyTitle("My dashboard");
		assertTextPresent("Dashboard of firstName03 lastName03");
		assertTextPresent("email03");
		assertElementNotPresentByXPath("//div[@id='main-section']//i[@class='icon-phone']");
	}

	@Test
	public void nav() {
		login("email02", "qqqq");
		clickLink("change-profile");
		verifyTitle("Edit my profile");
	}

}
