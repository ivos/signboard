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

@Setup({ "../deleteAll.xml", "EditUserTest.xml" })
@BaseUrl("http://localhost:8080/signboard")
public class EditUserTest extends ITBase {

	@Before
	public void before() {
		login("email01", "qqqq");
	}

	@Test
	@Verify
	public void fn() {
		edit("02", "System administrator");
		edit("03", "User");
		edit("04");
		edit("05", "User", "System administrator");
	}

	private void edit(String number, String... roles) {
		gotoPage("user");
		clickLinkWithExactText("email" + number);
		clickLink("main:edit");
		verifyTitle("Edit user");
		assertSelectOptionsEqual("edit:systemRoles", new String[] { "User",
				"System administrator" });
		List<String> rolesBefore = Arrays.asList(roles);
		if (!rolesBefore.contains("User")) {
			unselectRole("User");
		}
		if (!rolesBefore.contains("System administrator")) {
			unselectRole("System administrator");
		}
		selectOptions("edit:systemRoles", roles);
		clickButton("edit:save");
		verifyAction("Saved.");
	}

	private void unselectRole(String label) {
		getTestingEngine().unselectOptions("edit:systemRoles",
				new String[] { label });
	}

	@Test
	@Verify
	public void val_KeepSystemAdminOnSelf() {
		gotoPage("user");
		clickLinkWithExactText("email01");
		clickLink("main:edit");
		setWorkingForm("edit");
		unselectRole("System administrator");
		clickButton("edit:save");
		assertTextPresent("Cannot remove system administrator role on your own user.");
		verifyTitle("Edit user");

		setWorkingForm("edit");
		unselectRole("User");
		selectOption("edit:systemRoles", "System administrator");
		clickButton("edit:save");
		verifyAction("Saved.");
	}

	@Test
	public void sec_MustBeSystemAdministrator() {
		login("email02", "qqqq");
		gotoPage("user/3/edit");
		assertAccessDenied();
	}

}
