package it.projectmember;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Before;
import org.junit.Test;

@Setup({ "../deleteAll.xml", "DisableAndActivateProjectMemberTest.xml" })
@BaseUrl("http://localhost:8080/signboard")
public class DisableAndActivateProjectMemberTest extends ITBase {

	@Before
	public void before() {
		login("email02", "qqqq");
	}

	@Test
	@Verify
	public void fn() {
		// active to disabled
		gotoPage("projectMember/22");
		verifyTitle("View project member");
		assertTextPresent("Status Active");
		assertElementPresentByXPath("//button[@id='main:activate' and @disabled='disabled']");
		clickButton("main:disable");
		verifyAction("Saved.");
		assertTextPresent("Status Disabled");
		verifyTitle("View project member");

		// pending to active
		gotoPage("projectMember/23");
		assertTextPresent("Status Pending");
		clickButton("main:activate");
		verifyAction("Saved.");
		assertTextPresent("Status Active");

		// pending to disabled
		gotoPage("projectMember/24");
		assertTextPresent("Status Pending");
		clickButton("main:disable");
		verifyAction("Saved.");
		assertTextPresent("Status Disabled");

		// disabled to active
		gotoPage("projectMember/25");
		assertTextPresent("Status Disabled");
		assertElementPresentByXPath("//button[@id='main:disable' and @disabled='disabled']");
		clickButton("main:activate");
		verifyAction("Saved.");
		assertTextPresent("Status Active");
	}

	@Test
	public void sec_CannotDisableOwnMember() {
		gotoPage("projectMember/21");
		assertTextPresent("Status Active");
		assertElementPresentByXPath("//button[@id='main:activate' and @disabled='disabled']");
		assertElementPresentByXPath("//button[@id='main:disable' and @disabled='disabled']");
	}

	@Test
	public void sec_MustBeProjectAdministrator() {
		// active project user
		login("email03", "qqqq");
		gotoPage("projectMember/23");
		assertElementNotPresentByXPath("//button[@id='main:activate']");
		assertElementNotPresentByXPath("//button[@id='main:disable']");
	}

	@Test
	public void sec_MustBeActiveProjectAdministrator_Pending() {
		// pending project administrator
		login("email05", "qqqq");
		gotoPage("projectMember/23");
		assertAccessDenied();
	}

	@Test
	public void sec_MustBeActiveProjectAdministrator_Disabled() {
		// disabled project administrator
		login("email06", "qqqq");
		gotoPage("projectMember/23");
		assertAccessDenied();
	}

}
