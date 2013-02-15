package it.projectmember;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;

import org.junit.Before;
import org.junit.Test;

@Setup({ "../deleteAll.xml", "ViewProjectMemberTest.xml" })
@BaseUrl("http://localhost:8080/signboard")
public class ViewProjectMemberTest extends ITBase {

	@Before
	public void before() {
		login("email02", "qqqq");
	}

	@Test
	public void fn() {
		view(3, "Active", "Project administrator");
		view(4, "Pending", "User");
		view(5, "Pending", "Project administrator");
		view(6, "Disabled", "User");
		view(7, "Disabled", "Project administrator");
		view(8, "Active", "User", "Project administrator");
	}

	private void view(int number, String status, String... roles) {
		gotoPage("projectMember/" + (number + 19));
		verifyTitle("View project member");
		assertTextPresent("Project name2");
		assertTextPresent("Project member firstName0" + number + " lastName0"
				+ number);
		assertTextPresent("email0" + number);
		assertTextPresent("phone0" + number);
		assertTextPresent("Skype skype0" + number);
		assertTextPresent("Registered Jan " + number + ", 2010");
		assertTextPresent("Last login Jan 31, 2010 1:01:0" + number + " AM");
		assertTextPresent("Status " + status);
		if (1 == roles.length) {
			assertTextPresent("Roles " + roles[0]);
		} else {
			for (String role : roles) {
				assertTextPresent(role);
			}
		}
	}

	@Test
	public void fn_OtherProject() {
		login("email01", "qqqq");
		gotoPage("projectMember/11");
		verifyTitle("View project member");
		assertTextPresent("Project name1");
		assertTextPresent("Project member firstName01 lastName01");
		assertTextPresent("email01");
		assertTextPresent("phone01");
		assertTextPresent("Skype skype01");
		assertTextPresent("Registered Jan 1, 2010");
		assertTextPresent("Status Active");
		assertTextPresent("Roles Project administrator");
	}

	@Test
	public void fn_OptionalFields() {
		gotoPage("projectMember/28");
		verifyTitle("View project member");
		assertTextPresent("Project member firstName09 lastName09");
		assertTextPresent("email09");
		assertTextNotPresent("phone");
		assertTextNotPresent("Skype");
		assertTextPresent("Registered Jan 9, 2010");
		assertTextNotPresent("Last login");
		assertTextPresent("Status Active");
		assertTextPresent("Roles None");
	}

	@Test
	public void nav() {
		gotoPage("project/code2/member");
		clickLinkWithExactText("email03");

		verifyTitle("View project member");
		assertTextPresent("Project name2");
		assertTextPresent("Project member firstName03 lastName03");
		verifyURL("projectMember/22");
	}

	@Test
	public void sec_MustBeProjectMember() {
		// member of other project
		gotoPage("projectMember/11");
		assertAccessDenied();
	}

	@Test
	public void sec_MustBeActiveProjectMember() {
		// pending user member
		login("email04", "qqqq");
		gotoPage("projectMember/21");
		assertAccessDenied();
	}

}
