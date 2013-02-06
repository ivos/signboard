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
		assertSelectedOptionEquals("main:status", "Active");
		assertElementPresentByXPath("//button[@id='main:activate' and @disabled='disabled']");
		clickButton("main:disable");
		verifyAction("Saved.");
		assertSelectedOptionEquals("main:status", "Disabled");
		verifyTitle("View user");

		// disabled to active
		gotoPage("user");
		clickLinkWithExactText("email03");
		assertSelectedOptionEquals("main:status", "Disabled");
		assertElementPresentByXPath("//button[@id='main:disable' and @disabled='disabled']");
		clickButton("main:activate");
		verifyAction("Saved.");
		assertSelectedOptionEquals("main:status", "Active");
	}

	@Test
	public void sec_CannotDisableOwnAccount() {
		gotoPage("user");
		clickLinkWithExactText("email01");
		assertSelectedOptionEquals("main:status", "Active");
		assertElementPresentByXPath("//button[@id='main:activate' and @disabled='disabled']");
		assertElementPresentByXPath("//button[@id='main:disable' and @disabled='disabled']");
	}

}
