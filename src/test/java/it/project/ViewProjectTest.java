package it.project;

import static junit.framework.Assert.*;
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

	private void navigate(String number) {
		gotoPage("project");
		clickLinkWithExactText("code" + number);
		verifyTitle("View project");
		assertTextPresent("View project");
	}

	private void viewNonActiveMember(String number) {
		navigate(number);
		assertTextPresent("code" + number);
		assertTextPresent("name" + number);
		assertTextNotPresent("description" + number);
		assertTextNotPresent("Created on");
	}

	private void viewActiveMember(String number) {
		navigate(number);
		assertTextPresent("code" + number);
		assertTextPresent("name" + number);
		assertTextPresent("description" + number);
		assertTextPresent("Jan " + number + ", 2012");
	}

	@Test
	public void fn() {
		viewActiveMember("1");
		viewNonActiveMember("2");
		viewNonActiveMember("3");
		viewActiveMember("4");
	}

	@Test
	public void fn_OtherUser() {
		login("email3", "qqqq");
		viewNonActiveMember("1");
		viewActiveMember("2");
		viewNonActiveMember("3");
		viewActiveMember("4");
	}

	@Test
	public void nav() {
		gotoPage("/");
		clickLinkWithExactText("Project");
		clickLinkWithExactText("code2");
		verifyTitle("View project");
		assertTrue(getTestingEngine().getPageURL().toString()
				.endsWith("/signboard/project/code2"));
		assertTextPresent("View project");
	}

	@Test
	public void sec_MustBeSystemUser() {
		login("email2", "qqqq");
		gotoPage("project/code2");
		assertAccessDenied();
	}

}
