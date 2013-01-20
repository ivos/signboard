package it.user;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;

import java.util.Arrays;
import java.util.List;

import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Before;
import org.junit.Test;

@Setup
@Verify
@BaseUrl("http://localhost:8080/signboard")
public class EditUserTest extends ITBase {

	@Before
	public void before() {
		login("email01", "qqqq");
	}

	@Test
	public void fn() {
		edit("02", "System administrator");
		edit("03", "User");
		edit("04");
		edit("05", "User", "System administrator");
	}

	private void edit(String number, String... roles) {
		gotoPage("user");
		clickLinkWithExactText("email" + number);
		clickLinkWithText("Edit");
		assertTextPresent("Edit user");
		assertTextPresent("email" + number);
		assertSelectOptionsEqual("edit:systemRoles", new String[] { "User",
				"System administrator" });
		List<String> rolesBefore = Arrays.asList(roles);
		if (!rolesBefore.contains("User")) {
			getTestingEngine().unselectOptions("edit:systemRoles",
					new String[] { "User" });
		}
		if (!rolesBefore.contains("System administrator")) {
			getTestingEngine().unselectOptions("edit:systemRoles",
					new String[] { "System administrator" });
		}
		selectOptions("edit:systemRoles", roles);
		clickButton("edit:save");
		verifyAction("Saved.");
	}

	@Test
	@Verify("EditUserTest.xml")
	public void sec_DeniedForNonSystemAdministrator() {
		login("email02", "qqqq");
		gotoPage("user/3/edit");
		assertAccessDenied();
	}

}
