package it.task;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;

import org.junit.Before;
import org.junit.Test;

@Setup({ "../deleteAll.xml", "../users.xml", "../projects.xml",
		"ViewTaskTest.xml" })
@BaseUrl("http://localhost:8080/signboard")
public class ViewTaskTest extends ITBase {

	@Before
	public void before() {
		login("email1", "qqqq");
	}

	private void view(int number, String project, String author) {
		gotoPage("task/" + number);
		verifyTitle("View task");
		assertTextPresent("Project " + project);
		assertTextPresent("Task goal0" + number);
		assertTextPresent("Created Jan " + number + ", 2013 2:03:01 AM");
		assertTextPresent("Author " + author);
	}

	@Test
	public void fn() {
		view(1, "name1", "f1 l1");
		view(2, "name5", "f2 l2");
		view(3, "name1", "f1 l1");
		view(4, "name5", "f1 l1");
	}

	@Test
	public void nav() {
		turnJavaScriptOff();
		try {
			gotoPage("task");
			clickLinkWithExactText("goal03");

			verifyTitle("View task");
			assertTextPresent("Project name1");
			assertTextPresent("Task goal03");
			verifyURL("task/3");
		} finally {
			turnJavaScriptOn();
		}
	}

	@Test
	public void sec_MustBeProjectMember() {
		// not member of the project
		gotoPage("task/5");
		assertAccessDenied();
	}

	@Test
	public void sec_MustBeProjectUser() {
		// not user of the project
		gotoPage("task/6");
		assertAccessDenied();
	}

	@Test
	public void sec_MustBeActiveProjectUser() {
		// not user of the project
		gotoPage("task/7");
		assertAccessDenied();
	}

}
