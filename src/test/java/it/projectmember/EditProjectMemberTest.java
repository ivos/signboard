package it.projectmember;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;

import java.util.Arrays;
import java.util.List;

import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Before;
import org.junit.Test;

@Setup({ "../deleteAll.xml", "EditProjectMemberTest.xml" })
@BaseUrl("http://localhost:8080/signboard")
public class EditProjectMemberTest extends ITBase {

	@Before
	public void before() {
		login("email02", "qqqq");
	}

	@Test
	@Verify
	public void fn() {
		edit(22, "Project administrator");
		edit(23, "User");
		edit(24);
		edit(25, "User", "Project administrator");
	}

	private void edit(int number, String... roles) {
		gotoPage("projectMember/" + number + "/edit");
		verifyTitle("Edit project member");
		assertSelectOptionsEqual("edit:roles", new String[] { "User",
				"Project administrator" });
		List<String> rolesBefore = Arrays.asList(roles);
		if (!rolesBefore.contains("User")) {
			unselectRole("User");
		}
		if (!rolesBefore.contains("Project administrator")) {
			unselectRole("Project administrator");
		}
		selectOptions("edit:roles", roles);
		clickButton("edit:save");
		verifyAction("Saved.");
	}

	private void unselectRole(String label) {
		getTestingEngine()
				.unselectOptions("edit:roles", new String[] { label });
	}

	@Test
	@Verify("EditProjectMemberTest.noChange-verify.xml")
	public void val_KeepSystemAdminOnSelf() {
		gotoPage("projectMember/21/edit");
		setWorkingForm("edit");
		unselectRole("Project administrator");
		clickButton("edit:save");
		assertTextNotPresent("Saved.");
		assertTextPresent("Cannot remove project administrator role on your own member.");
		verifyTitle("Edit project member");
	}

	@Test
	@Verify
	public void val_CanRemoveUserOnSelf() {
		gotoPage("projectMember/21/edit");
		setWorkingForm("edit");
		unselectRole("User");
		selectOption("edit:roles", "Project administrator");
		clickButton("edit:save");
		verifyAction("Saved.");
	}

	@Test
	@Verify("EditProjectMemberTest.noChange-verify.xml")
	public void nav() {
		gotoPage("projectMember/22");
		verifyTitle("View project member");
		assertTextPresent("Project name2");
		assertTextPresent("Project member firstName03 lastName03");
		clickLinkWithExactText("Edit");

		verifyTitle("Edit project member");
		assertTextPresent("Project name2");
		assertTextPresent("Project member firstName03 lastName03");
		verifyURL("projectMember/22/edit");
		clickButton("edit:save");

		verifyAction("Saved.");
		verifyTitle("View project member");
		assertTextPresent("Project member firstName03 lastName03");
		clickLinkWithExactText("Edit");

		verifyTitle("Edit project member");
		assertTextPresent("Project member firstName03 lastName03");
		clickLinkWithExactText("Cancel");

		verifyTitle("View project member");
		assertTextPresent("Project member firstName03 lastName03");
		assertTextNotPresent("Saved.");
	}

	@Test
	public void sec_HideEditButtonForProjectUser() {
		// user member
		login("email03", "qqqq");
		gotoPage("projectMember/21");
		assertElementNotPresentByXPath("//a[@id='main:edit']");
	}

	@Test
	public void sec_MustBeProjectAdministrator_User() {
		// user member
		login("email03", "qqqq");
		gotoPage("projectMember/21/edit");
		assertAccessDenied();
	}

	@Test
	public void sec_MustBeProjectAdministrator_OtherProjectAdmin() {
		// other project's admin
		gotoPage("projectMember/11/edit");
		assertAccessDenied();
	}

	@Test
	public void sec_MustBeActiveProjectAdministrator_Pending() {
		// pending admin
		login("email04", "qqqq");
		gotoPage("projectMember/22/edit");
		assertAccessDenied();
	}

	@Test
	public void sec_MustBeActiveProjectAdministrator_Disabled() {
		// disabled admin
		login("email05", "qqqq");
		gotoPage("projectMember/22/edit");
		assertAccessDenied();
	}

}
