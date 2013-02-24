package it.project;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;

import org.junit.Test;

@Setup({ "../deleteAll.xml", "../users.xml" })
@BaseUrl("http://localhost:8080/signboard")
public class ProjectOverviewTest extends ITBase {

	@Test
	public void fn() {
		login("email1", "qqqq");
		assertTextPresent("You are not a member of any project yet.");
		clickLink("create-project");

		setTextField("edit:code", "code1");
		setTextField("edit:name", "name1");
		clickButton("edit:save");
		verifyAction("Saved.");

		verifyTitle("View project");
		assertTextPresent("Project name1");
		assertTextPresent("Code code1");

		clickLinkWithExactText("Edit");
		verifyTitle("Edit project");
		setTextField("edit:name", "name2");
		setTextField("edit:description", "description2");
		clickButton("edit:save");
		verifyAction("Saved.");

		verifyTitle("View project");
		assertTextPresent("Project name2");
		assertTextPresent("Code code1");
		assertTextPresent("description2");

		clickLinkWithExactText("Home");
		clickLinkWithExactText("name2");

		verifyTitle("View project");
		assertTextPresent("Project name2");
	}

}
