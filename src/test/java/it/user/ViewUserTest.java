package it.user;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Before;
import org.junit.Test;

@Setup({ "../deleteAll.xml", "ViewUserTest.xml" })
@Verify
@BaseUrl("http://localhost:8080/signboard")
public class ViewUserTest extends ITBase {

	@Before
	public void before() {
		login("email01", "qqqq");
	}

	@Test
	public void fn() {
		view("2", "Active", "User");
		view("3", "Disabled", "System administrator");
		view("4", "Active", "User", "System administrator");
	}

	private void view(String number, String status, String... roles) {
		gotoPage("user");
		clickLinkWithExactText("email0" + number);
		verifyTitle("View user");
		assertTextPresent("User firstName0" + number + " lastName0" + number);
		assertTextPresent("email0" + number);
		assertTextPresent("phone0" + number);
		assertTextPresent("Skype skype0" + number);
		assertTextPresent("Registered Jan " + number + ", 2012");
		assertTextPresent("Last login May 1, 2012 12:59:0" + number + " PM");
		assertTextPresent("Status " + status);
		if (1 == roles.length) {
			assertTextPresent("System roles " + roles[0]);
		} else {
			for (String role : roles) {
				assertTextPresent(role);
			}
		}
	}

	@Test
	public void fn_OptionalFields() {
		gotoPage("user");
		clickLinkWithExactText("email05");
		verifyTitle("View user");
		assertTextPresent("User firstName05 lastName05");
		assertTextPresent("email05");
		assertTextNotPresent("phone");
		assertTextNotPresent("Skype");
		assertTextPresent("Registered Jan 5, 2012");
		assertTextNotPresent("Last login");
		assertTextPresent("Status Active");
		assertTextPresent("System roles None");
	}

	@Test
	public void sec_MustBeSystemAdministrator() {
		login("email02", "qqqq");
		gotoPage("user/3");
		assertAccessDenied();
	}

}
