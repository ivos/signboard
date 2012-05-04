package it.user;

import static junit.framework.Assert.*;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import net.sf.lightair.LightAir;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(LightAir.class)
@Setup
@Verify("LogInOutIT.xml")
@BaseUrl("http://localhost:8080/signboard")
public class LogInOutIT {

	@Test
	public void fn() {
		beginAt("login");
		assertNotLoggedIn();

		login("email2", "password2");
		clickLink("header-home");
		assertLoggedIn();

		clickLinkWithExactText("Log out");
		assertNotLoggedIn();
	}

	private void assertNotLoggedIn() {
		assertLinkPresentWithExactText("Log in");
		assertLinkPresentWithExactText("Register");
		assertLinkNotPresentWithExactText("Log out");
		assertTextNotPresent("email2");
	}

	private void assertLoggedIn() {
		assertTextPresent("email2");
		assertLinkPresentWithExactText("Log out");
		assertLinkNotPresentWithExactText("Log in");
	}

	private void login(String email, String password) {
		beginAt("login");
		setTextField("main:email", email);
		setTextField("main:password", password);
		submit();
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
		assertTitleEquals("Welcome - Signboard");
		assertTrue(getTestingEngine().getPageURL().toString()
				.endsWith("/signboard/"));
		clickLinkWithExactText("log in");
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
