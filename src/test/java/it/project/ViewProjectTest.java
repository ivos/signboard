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
		clickLinkWithExactText("code0" + number);
		verifyTitle("View project");
		assertTextPresent("View project");
	}

	private void viewNonMember(String number) {
		navigate(number);
		assertTextPresent("code0" + number);
		assertTextPresent("name0" + number);
		assertTextNotPresent("Description");
		assertTextNotPresent("Date created");
	}

	private void viewMember(String number) {
		navigate(number);
		assertTextPresent("code0" + number);
		assertTextPresent("name0" + number);
		assertTextPresent("description0" + number);
		assertTextPresent("Jan " + number + ", 2012");
	}

	@Test
	public void fn() {
		viewMember("1");
		viewNonMember("2");
		viewMember("3");
	}

	@Test
	public void fn_OtherUser() {
		login("email3", "qqqq");
		viewNonMember("1");
		viewMember("2");
		viewMember("3");
	}

	@Test
	public void nav() {
		gotoPage("/");
		clickLinkWithExactText("Project");
		clickLinkWithExactText("code02");
		verifyTitle("View project");
		assertTrue(getTestingEngine().getPageURL().toString()
				.endsWith("/signboard/project/code02"));
		assertTextPresent("View project");

		clickLinkWithExactText("View all");
		verifyTitle("Search projects");
		assertTrue(getTestingEngine().getPageURL().toString()
				.endsWith("/signboard/project"));
	}

	@Test
	public void sec_MustBeSystemUser() {
		login("email2", "qqqq");
		gotoPage("project/code2");
		assertAccessDenied();
	}

}
