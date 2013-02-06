package it.user;

import static junit.framework.Assert.*;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Test;

@Setup({ "../deleteAll.xml", "LogInOutTest.xml" })
@Verify("LogInOutTest.xml")
@BaseUrl("http://localhost:8080/signboard")
public class LogInOutTest extends ITBase {

	@Test
	@Verify
	public void fn_Login_Logout() {
		beginAt("login");
		assertNotLoggedIn();

		fillAndSubmitLoginForm("email2", "password2");
		clickLink("nav-form:nav-home");
		assertLoggedIn("2");

		clickLinkWithText("Log out");
		assertNotLoggedIn();
	}

	@Test
	@Verify
	public void fn_LoginWhenLoggedIn() {
		beginAt("login");

		fillAndSubmitLoginForm("email2", "password2");
		clickLink("nav-form:nav-home");
		assertLoggedIn("2");

		clickLinkWithExactText("Signboard");
		clickLinkWithExactText("log in");
		fillAndSubmitLoginForm("email1", "password1");
		clickLink("nav-form:nav-home");
		assertLoggedIn("1");
	}

	private void assertNotLoggedIn() {
		assertLinkPresentWithExactText("Log in");
		assertLinkPresentWithExactText("Register");
		assertLinkNotPresentWithText("Log out");
		assertTextNotPresent("email2");
	}

	private void assertLoggedIn(String number) {
		assertTextPresent("first" + number + " last" + number);
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
	public void val_InvalidEmail() {
		beginAt("login");
		assertNotLoggedIn();

		fillAndSubmitLoginForm("email2XXX", "password2");
		assertTextPresent("Login failed. The provided e-mail and password do not identify an active system user.");
	}

	@Test
	public void val_InvalidPassword() {
		beginAt("login");
		assertNotLoggedIn();

		fillAndSubmitLoginForm("email2", "password2XXX");
		assertTextPresent("Login failed. The provided e-mail and password do not identify an active system user.");
	}

	@Test
	public void val_UserNotActive() {
		beginAt("login");
		assertNotLoggedIn();

		fillAndSubmitLoginForm("email3", "password3");
		assertTextPresent("Login failed. The provided e-mail and password do not identify an active system user.");
	}

	@Test
	public void nav() {
		beginAt("/");
		setupAjax();
		verifyTitle("Welcome");
		assertTrue(getTestingEngine().getPageURL().toString()
				.endsWith("/signboard/"));
		clickLinkWithExactText("Log in");
		verifyTitle("Log in");
		assertTrue(getTestingEngine().getPageURL().toString()
				.contains("/signboard/login"));
		assertTextPresent("Enter your login data to start using the system.");
		clickLinkWithExactText("Cancel");
		verifyTitle("Welcome");
		assertTrue(getTestingEngine().getPageURL().toString()
				.endsWith("/signboard/"));
	}

}
