package it.project;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Before;
import org.junit.Test;

@Setup({ "../deleteAll.xml", "../users.xml", "ViewProjectTest.xml" })
@Verify("ViewProjectTest.xml")
@BaseUrl("http://localhost:8080/signboard")
public class ViewProjectTest extends ITBase {

	@Before
	public void before() {
		login("email1", "qqqq");
	}

	private void viewActiveMember(String number, String status, String role) {
		gotoPage("project/code" + number);
		verifyTitle("View project");
		assertTextPresent("Project name" + number);
		assertTextPresent("Code code" + number);
		assertTextPresent("description" + number);
		assertTextPresent("Created on Jan " + number + ", 2012");
		assertTextPresent("You are " + status + " member of the project.");
		if (null != role) {
			assertTextPresent("You have roles " + role + " in the project.");
		}
		assertLinkPresentWithExactText("Members");
		assertButtonNotPresent("view:join");
	}

	private void viewNonActiveMember(String number, String status, String role) {
		gotoPage("project/code" + number);
		verifyTitle("View project");
		assertTextPresent("Project name" + number);
		assertTextPresent("Code code" + number);
		assertTextNotPresent("description" + number);
		assertTextNotPresent("Created on");
		assertTextPresent("You are " + status + " member of the project.");
		if (null != role) {
			assertTextPresent("You have roles " + role + " in the project.");
		}
		assertLinkNotPresentWithExactText("Members");
		assertButtonNotPresent("view:join");
	}

	private void viewNonMember(String number) {
		gotoPage("project/code" + number);
		verifyTitle("View project");
		assertTextPresent("Project name" + number);
		assertTextPresent("Code code" + number);
		assertTextNotPresent("description" + number);
		assertTextNotPresent("Created on");
		assertTextNotPresent("member of the project.");
		assertTextNotPresent("You have roles");
		assertLinkNotPresentWithExactText("Members");
		assertButtonPresent("view:join");
	}

	@Test
	public void fn() {
		viewActiveMember("1", "Active", "Project administrator");
		viewNonMember("2");
		viewNonActiveMember("3", "Pending", "User");
		viewActiveMember("4", "Active", "User");
	}

	@Test
	public void fn_OtherUser() {
		login("email3", "qqqq");
		viewNonMember("1");
		viewActiveMember("2", "Active", "User");

		viewNonActiveMember("3", "Disabled", null);
		assertTextPresent("You have roles");
		assertTextPresent("User");
		assertTextPresent("Project administrator");

		viewActiveMember("4", "Active", "None");
	}

	@Test
	public void nav() {
		gotoPage("/project");
		clickLinkWithExactText("code2");

		verifyTitle("View project");
		verifyURL("/signboard/project/code2");
	}

	@Test
	public void sec_MustBeSystemUser() {
		login("email2", "qqqq");
		gotoPage("project/code2");
		assertAccessDenied();
	}

}
