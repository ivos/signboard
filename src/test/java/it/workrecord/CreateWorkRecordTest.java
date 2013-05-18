package it.workrecord;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Setup({ "../deleteAll.xml", "../users.xml", "../projects.xml", "../tasks.xml" })
@Verify("CreateWorkRecordTest-empty.xml")
@BaseUrl("http://localhost:8080/signboard")
public class CreateWorkRecordTest extends ITBase {

	@Before
	public void before() {
		login("email1", "qqqq");
		turnJavaScriptOff();
	}

	@After
	public void after() {
		turnJavaScriptOn();
	}

	private void create(int taskId, String duration, String date) {
		gotoPage("task/" + taskId + "/work");
		setTextField("edit:duration", duration);
		if (null != date) {
			setTextField("edit:date", date);
		}
		clickButton("edit:save");
	}

	private void createOk(int taskId, String duration, String date) {
		create(taskId, duration, date);
		verifyAction("Saved.");
	}

	@Test
	@Verify
	public void fn() {
		createOk(1, "1", "01/01/2013");
		createOk(1, "2 35", "01/02/2013");
	}

	@Test
	@Verify
	public void fn_OtherUser() {
		login("email3", "qqqq");
		createOk(3, "1", "01/01/2013");
	}

	@Test
	@Verify
	public void fn_DefaultedFields() {
		createOk(1, "1", null);
	}

	@Test
	public void val_MandatoryFields() {
		create(1, "", "01/01/2013");
		assertTextPresent("May not be empty.");
		create(1, "1", "");
		assertTextPresent("May not be empty.");
	}

	@Test
	@Verify("CreateWorkRecordTest.fn_DefaultedFields-verify.xml")
	public void nav_AccessFromTaskView() {
		gotoPage("/task/1");
		clickLinkWithExactText("Record work");
		verifyTitle("Create work record");
		verifyURL("/task/1/work");

		setTextField("edit:duration", "1");
		clickButton("edit:save");
	}

	@Test
	public void nav_Cancel() {
		gotoPage("/task/2/work");
		clickLinkWithExactText("Cancel");

		verifyTitle("View task");
		verifyURL("/task/2");
	}

	@Test
	public void sec_MustBeActiveProjectUser() {
		gotoPage("task/3/work");
		assertAccessDenied();
	}

}
