package it.project;

import static junit.framework.Assert.*;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Setup({ "../users.xml", "SearchProjectTest.xml" })
@Verify("SearchProjectTest.xml")
@BaseUrl("http://localhost:8080/signboard")
public class SearchProjectTest extends ITBase {

	@Before
	public void before() {
		login("email1", "password1");
		turnJavaScriptOff();
	}

	@After
	public void after() {
		turnJavaScriptOn();
	}

	private void search(String name, String code) {
		setTextField("search:name", name);
		setTextField("search:code", code);
		clickButton("search:search");
	}

	@Test
	public void fn_Paging_NoSearch() {
		gotoPage("project");
		search("", "");
		assertTextPresent("1 .. 10 of 21");
		assertTextPresent("code01");
		assertTextPresent("code02");
		assertTextPresent("code10");
		clickLinkWithExactText("2");
		assertTextPresent("11 .. 20 of 21");
		assertTextPresent("code11");
		assertTextPresent("code12");
		assertTextPresent("code20");
		clickLinkWithExactText("3");
		assertTextPresent("21 .. 21 of 21");
		assertTextPresent("code21");
	}

	@Test
	public void fn_Search() {
		gotoPage("project");
		search("1", "");
		assertTextPresent("1 .. 10 of 12");
		assertTextPresent("code01");
		assertTextPresent("code10");
		assertTextPresent("code18");
		clickLinkWithExactText("2");
		assertTextPresent("11 .. 12 of 12");
		assertTextPresent("code19");
		assertTextPresent("code21");

		search("", "0");
		assertTextPresent("1 .. 10 of 11");
		assertTextPresent("code01");
		assertTextPresent("code02");
		assertTextPresent("code10");
		clickLinkWithExactText("2");
		assertTextPresent("11 .. 11 of 11");
		assertTextPresent("code20");

		search("1", "0");
		assertTextPresent("code01");
		assertTextPresent("code10");

		clickButton("search:reset");
		assertTextPresent("1 .. 10 of 21");
	}

	@Test
	public void fn_Columns() {
		gotoPage("project");
		search("1", "0");
		assertTableEquals("search:projectListBeanPageItems", new String[][] {
				{ "Code", "Name" }, { "code01", "name01" },
				{ "code10", "name10" } });
	}

	@Test
	public void nav() {
		gotoPage("/");
		verifyTitle("Welcome");
		assertTrue(getTestingEngine().getPageURL().toString()
				.endsWith("/signboard/"));

		clickLinkWithExactText("Project");
		verifyTitle("Search projects");
		assertTrue(getTestingEngine().getPageURL().toString()
				.endsWith("/signboard/project"));
	}

	@Test
	public void sec_MustBeLoggedIn() {
		beginAt("/project");
		verifyTitle("Log in");
		fillAndSubmitLoginForm("email1", "password1");
		gotoPage("/project");
		verifyTitle("Search projects");
	}

	@Test
	public void sec_MustBeSystemUser() {
		login("email2", "password2");
		gotoPage("project");
		assertAccessDenied();
	}

}
