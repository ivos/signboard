package it.task;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Before;
import org.junit.Test;

@Setup({ "../deleteAll.xml", "../users.xml", "../projects.xml",
		"EditTaskTest.xml" })
@BaseUrl("http://localhost:8080/signboard")
public class EditTaskTest extends ITBase {

	@Before
	public void before() {
		login("email1", "qqqq");
	}

	private void edit(int iNumber, String number, String projectName) {
		gotoPage("task/" + iNumber + "/edit");
		verifyTitle("Edit task");
		assertTextPresent("Project " + projectName);
		assertTextFieldEquals("edit:goal", "goal" + number);
		assertTextFieldEquals("edit:description", "description" + number);
		setTextField("edit:goal", "goal" + number + "a");
		setTextField("edit:description", "description" + number + "b");
		clickButton("edit:save");
		verifyAction("Saved.");
		verifyTitle("View task");
	}

	@Test
	@Verify
	public void fn() {
		edit(1, "01", "name1");
		edit(2, "02", "name5");
		edit(3, "03", "name1");
	}

	@Test
	@Verify
	public void fn_OtherUser() {
		login("email3", "qqqq");
		edit(5, "05", "name2");
	}

	@Test
	public void nav_Cancel() {
		turnJavaScriptOff();
		try {
			gotoPage("task");
			clickLinkWithText("goal01");
			verifyTitle("View task");
			clickLinkWithExactText("Edit");
			verifyTitle("Edit task");
			clickLinkWithExactText("Cancel");
			verifyTitle("View task");
		} finally {
			turnJavaScriptOn();
		}
	}

	@Test
	public void sec_MustBeProjectMember() {
		// not member of the project
		gotoPage("task/5/edit");
		assertAccessDenied();
	}

	@Test
	public void sec_MustBeProjectUser() {
		// not user of the project
		gotoPage("task/6/edit");
		assertAccessDenied();
	}

	@Test
	public void sec_MustBeActiveProjectUser() {
		// not user of the project
		gotoPage("task/7/edit");
		assertAccessDenied();
	}

}
