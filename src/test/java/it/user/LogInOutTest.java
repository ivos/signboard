package it.user;

import static junit.framework.Assert.*;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Test;

@Setup
@Verify("LogInOutTest.xml")
@BaseUrl("http://localhost:8080/signboard")
public class LogInOutTest extends ITBase {

	@Test
	public void fn() {
		beginAt("login");
		assertNotLoggedIn();

		login("email2", "password2");
		clickLink("nav-home");
		assertLoggedIn();

		clickLinkWithText("Log out");
		assertNotLoggedIn();
	}

	private void assertNotLoggedIn() {
		assertLinkPresentWithExactText("Log in");
		assertLinkPresentWithExactText("Register");
		assertLinkNotPresentWithText("Log out");
		assertTextNotPresent("email2");
	}

	private void assertLoggedIn() {
		assertTextPresent("first2 last2");
		assertLinkPresentWithText("Log out");
		assertLinkNotPresentWithText("Log in");
	}

	@Test
	public void val_MandatoryFields() {
		login("", "password2");
		assertTextPresent("May not be empty.");
		login("email2", "");
		assertTextPresent("May not be empty.");
	}

	@Test
	public void nav() {
		beginAt("/");
		setupAjax();
		assertTitleEquals("Welcome - Signboard");
		assertTrue(getTestingEngine().getPageURL().toString()
				.endsWith("/signboard/"));
		clickLinkWithExactText("Log in");
		assertTitleEquals("Log in - Signboard");
		assertTrue(getTestingEngine().getPageURL().toString()
				.contains("/signboard/login"));
		assertTextPresent("Enter your login data to start using the system.");
		clickLinkWithExactText("Cancel");
		assertTitleEquals("Welcome - Signboard");
		assertTrue(getTestingEngine().getPageURL().toString()
				.endsWith("/signboard/"));
	}

}
