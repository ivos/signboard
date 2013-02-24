package it.project;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Before;
import org.junit.Test;

@Setup({ "../deleteAll.xml", "../users.xml", "JoinProjectTest.xml" })
@Verify
@BaseUrl("http://localhost:8080/signboard")
public class JoinProjectTest extends ITBase {

	@Before
	public void before() {
		login("email1", "qqqq");
	}

	@Test
	public void fn() {
		gotoPage("project/code2");
		verifyTitle("View project");
		assertTextPresent("Project name2");
		assertTextPresent("Code code2");
		assertTextNotPresent("description2");
		clickButton("view:join");
		verifyAction("Your request to join the project has been created. "
				+ "The project's administrator must confirm your membership now.");
		assertTextNotPresent("description2");
		assertButtonNotPresent("view:join");
	}

}
