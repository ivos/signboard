package it.task;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Before;
import org.junit.Test;

@Setup({ "../deleteAll.xml", "../users.xml", "../projects.xml" })
@Verify("CreateTaskTest-empty.xml")
@BaseUrl("http://localhost:8080/signboard")
public class CreateTaskTest extends ITBase {

	@Before
	public void before() {
		login("email1", "qqqq");
	}

	private void create(String project, String goal, String description) {
		gotoPage("task/create");
		setTextField("edit:project", project);
		setTextField("edit:goal", goal);
		setTextField("edit:description", description);
		clickButton("edit:save");
	}

	private void createOk(String project, String goal, String description) {
		create(project, goal, description);
		verifyAction("Saved.");
	}

	@Test
	@Verify
	public void fn() {
		createOk("code1", "goal1", "description1");
		createOk("code5", "goal2", "description2");
	}

	@Test
	@Verify
	public void fn_OtherUser() {
		login("email3", "qqqq");
		createOk("code2", "goal1", "description1");
	}

	@Test
	@Verify
	public void fn_OptionalFields() {
		createOk("code1", "goal1", "");
	}

	@Test
	public void val_MandatoryFields() {
		create("", "goal1", "description1");
		assertTextPresent("May not be empty.");
		create("code1", "", "description1");
		assertTextPresent("May not be empty.");
	}

	// @Test
	// @Verify("CreateTaskTest.fn_OptionalFields-verify.xml")
	public void nav() {
		// TODO
	}

	@Test
	public void sec_MustBeActiveProjectUser() {
		gotoPage("task/create");
		assertSelectOptionValuesEqual("edit:project-original", new String[] {
				"", "code1", "code5", "code6" });
	}

}
