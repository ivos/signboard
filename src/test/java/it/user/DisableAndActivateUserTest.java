package it.user;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Before;
import org.junit.Test;

@Setup({ "../deleteAll.xml", "DisableAndActivateUserTest.xml" })
@Verify
@BaseUrl("http://localhost:8080/signboard")
public class DisableAndActivateUserTest extends ITBase {

	@Before
	public void before() {
		login("email01", "qqqq");
	}

	@Test
	public void fn() {
		// active to disabled
		gotoPage("user");
		clickLinkWithExactText("email02");
		verifyTitle("View user");
		assertTextPresent("Status Active");
		assertElementPresentByXPath("//button[@id='main:activate' and @disabled='disabled']");
		clickButton("main:disable");
		verifyAction("Saved.");
		assertTextPresent("Status Disabled");
		verifyTitle("View user");

		// disabled to active
		gotoPage("user");
		clickLinkWithExactText("email03");
		assertTextPresent("Status Disabled");
		assertElementPresentByXPath("//button[@id='main:disable' and @disabled='disabled']");
		clickButton("main:activate");
		verifyAction("Saved.");
		assertTextPresent("Status Active");
	}

	@Test
	public void sec_CannotDisableOwnAccount() {
		gotoPage("user");
		clickLinkWithExactText("email01");
		assertTextPresent("Status Active");
		assertElementPresentByXPath("//button[@id='main:activate' and @disabled='disabled']");
		assertElementPresentByXPath("//button[@id='main:disable' and @disabled='disabled']");
	}

}
