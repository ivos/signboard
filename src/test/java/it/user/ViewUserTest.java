package it.user;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Before;
import org.junit.Test;

@Setup
@Verify("ViewUserTest.xml")
@BaseUrl("http://localhost:8080/signboard")
public class ViewUserTest extends ITBase {

	@Before
	public void before() {
		login("email01", "qqqq");
	}

	@Test
	public void fn() {
		view("02", "User");
		view("03", "System administrator");
		view("04", "User", "System administrator");
	}

	private void view(String number, String... roles) {
		gotoPage("user");
		clickLinkWithExactText("email" + number);
		assertTextPresent("View user");
		assertTextPresent("lastName" + number);
		assertTextPresent("firstName" + number);
		assertTextPresent("email" + number);
		assertTextPresent("phone" + number);
		assertTextPresent("skype" + number);
		assertSelectOptionsEqual("main:systemRoles", new String[] { "User",
				"System administrator" });
		assertSelectedOptionsEqual("main:systemRoles", roles);
	}

	@Test
	public void sec_DeniedForNonSystemAdministrator() {
		login("email02", "qqqq");
		gotoPage("user/3");
		assertAccessDenied();
	}

}
