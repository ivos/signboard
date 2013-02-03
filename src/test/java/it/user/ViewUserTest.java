package it.user;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Before;
import org.junit.Test;

@Setup
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
		assertTextPresent("View user");
		assertTextPresent("lastName0" + number);
		assertTextPresent("firstName0" + number);
		assertTextPresent("email0" + number);
		assertTextPresent("phone0" + number);
		assertTextPresent("skype0" + number);
		assertTextPresent("Jan " + number + ", 2012");
		assertTextPresent("May 1, 2012 12:59:0" + number + " PM");
		assertSelectedOptionEquals("main:status", status);
		assertSelectOptionsEqual("main:systemRoles", new String[] { "User",
				"System administrator" });
		assertSelectedOptionsEqual("main:systemRoles", roles);
	}

	@Test
	public void sec_MustBeSystemAdministrator() {
		login("email02", "qqqq");
		gotoPage("user/3");
		assertAccessDenied();
	}

}
