package it.projectmember;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Setup({ "../deleteAll.xml", "SearchProjectMemberTest.xml" })
@BaseUrl("http://localhost:8080/signboard")
public class SearchProjectMemberTest extends ITBase {

	@Before
	public void before() {
		login("email02", "qqqq");
		gotoPage("project/code2/member");
		turnJavaScriptOff();
	}

	@After
	public void after() {
		turnJavaScriptOn();
	}

	private void search(String lastName, String firstName, String email,
			String phone, String status, String registeredFrom,
			String registeredTo, String lastLoginFrom, String lastLoginTo) {
		setTextField("search:lastName", lastName);
		setTextField("search:firstName", firstName);
		setTextField("search:email", email);
		setTextField("search:phone", phone);
		selectOption("search:status", status);
		setTextField("search:registered", registeredFrom);
		setTextField("search:registered__To", registeredTo);
		setTextField("search:lastLogin", lastLoginFrom);
		setTextField("search:lastLogin__To", lastLoginTo);
		clickButton("search:search");
	}

	@Test
	public void fn_Paging_NoSearch() {
		search("", "", "", "", "", "", "", "", "");
		assertTextPresent("1 .. 10 of 11");
		assertTextPresent("phone02");
		assertTextPresent("phone03");
		assertTextPresent("phone10");
		assertTextPresent("phone11");
		clickLinkWithExactText("2");
		assertTextPresent("11 .. 11 of 11");
		assertTextPresent("phone12");
		verifyURL("/signboard/project/code2/member/page/2");
	}

	@Test
	public void fn_SearchByNames() {
		search("1", "", "", "", "", "", "", "", "");
		assertTextPresent("phone10");
		assertTextPresent("phone11");
		assertTextPresent("phone12");
		assertTextPresent("1 .. 3 of 3");

		search("", "0", "", "", "", "", "", "", "");
		assertTextPresent("phone02");
		assertTextPresent("phone03");
		assertTextPresent("phone09");
		assertTextPresent("phone10");
		assertTextPresent("1 .. 9 of 9");

		search("1", "0", "", "", "", "", "", "", "");
		assertTextPresent("phone10");
		assertTextPresent("1 .. 1 of 1");

		clickButton("search:reset");
		assertTextPresent("1 .. 10 of 11");
	}

	@Test
	public void fn_SearchByStatus() {
		search("", "", "", "", "Active", "", "", "", "");
		assertTextPresent("phone02");
		assertTextPresent("phone03");
		assertTextPresent("phone08");
		assertTextPresent("phone10");
		assertTextPresent("phone11");
		assertTextPresent("phone12");
		assertTextPresent("1 .. 6 of 6");

		search("", "", "", "", "Pending", "", "", "", "");
		assertTextPresent("phone04");
		assertTextPresent("phone05");
		assertTextPresent("1 .. 2 of 2");

		search("", "", "", "", "Disabled", "", "", "", "");
		assertTextPresent("phone06");
		assertTextPresent("phone07");
		assertTextPresent("phone09");
		assertTextPresent("1 .. 3 of 3");

		clickButton("search:reset");
		assertTextPresent("1 .. 10 of 11");
	}

	@Test
	public void fn_SearchByDates() {
		search("", "", "", "", "", "01/26/2010", "02/02/2010", "01/02/2010",
				"02/01/2010");
		assertTextPresent("phone03");
		assertTextPresent("phone04");
		assertTextPresent("phone06");
		assertTextPresent("1 .. 3 of 3");
	}

	@Test
	public void fn_Sort() {
		selectOption("search:sort", "By recent registration");
		search("1", "", "", "", "", "", "", "", "");
		assertTableEquals("search:projectMemberListBeanPageItems",
				new String[][] {
						{ "Last name", "First name", "E-mail", "Phone",
								"Status" },
						{ "lastName11", "firstName11", "email11", "phone11",
								"Active" },
						{ "lastName12", "firstName12", "email12", "phone12",
								"Active" },
						{ "lastName10", "firstName10", "email10", "phone10",
								"Active" } });

		selectOption("search:sort", "By recent login");
		search("", "", "", "", "Disabled", "", "", "", "");
		assertTableEquals("search:projectMemberListBeanPageItems",
				new String[][] {
						{ "Last name", "First name", "E-mail", "Phone",
								"Status" },
						{ "lastName09", "firstName09", "email09", "phone09",
								"Disabled" },
						{ "lastName06", "firstName06", "email06", "phone06",
								"Disabled" },
						{ "lastName07", "firstName07", "email07", "phone07",
								"Disabled" } });
	}

	@Test
	public void fn_Columns() {
		search("1", "0", "", "", "", "", "", "", "");
		assertTableEquals("search:projectMemberListBeanPageItems",
				new String[][] {
						{ "Last name", "First name", "E-mail", "Phone",
								"Status" },
						{ "lastName10", "firstName10", "email10", "phone10",
								"Active" } });
	}

	@Test
	public void fn_OtherProject() {
		login("email01", "qqqq");
		gotoPage("project/code1/member");
		verifyTitle("Search project members");
		assertTextPresent("Project name1");
		assertTextPresent("email01");
		assertTextPresent("1 .. 1 of 1");
	}

	@Test
	public void val_SearchDatesInterval() {
		search("", "", "", "", "", "01/26/2010", "01/25/2010", "", "");
		assertTextPresent("The end date must be after the start date.");
		search("", "", "", "", "", "01/26/2010", "01/26/2010", "", "");
		assertTextPresent("The end date must be after the start date.");
		search("", "", "", "", "", "01/26/2010", "01/27/2010", "", "");
		assertTextNotPresent("The end date must be after the start date.");
	}

	@Test
	public void val_RegisteredMustBeInPast() {
		search("", "", "", "", "", "01/01/2050", "", "", "");
		assertTextPresent("Must be in the past.");
		search("", "", "", "", "", "01/01/2010", "", "", "");
		assertTextNotPresent("Must be in the past.");
	}

	@Test
	public void val_LastLoginMustBeInPast() {
		search("", "", "", "", "", "", "", "01/01/2050", "");
		assertTextPresent("Must be in the past.");
		search("", "", "", "", "", "", "", "01/01/2010", "");
		assertTextNotPresent("Must be in the past.");
	}

	@Test
	public void sec_DeniedForNonMember() {
		login("email01", "qqqq");
		gotoPage("project/code2/member");
		assertAccessDenied();
	}

	@Test
	public void sec_DeniedForPendingMember() {
		login("email04", "qqqq");
		gotoPage("project/code2/member");
		assertAccessDenied();
	}

	@Test
	public void sec_DeniedForDisabledMember() {
		login("email06", "qqqq");
		gotoPage("project/code2/member");
		assertAccessDenied();
	}

	@Test
	public void sec_LoadProjectFromMemberInSecurityCheck() {
		verifyTitle("Search project members");
		gotoPage("login");
		fillAndSubmitLoginForm("email01", "qqqq");
		gotoPage("project/code1/member");
		verifyTitle("Search project members");
	}

}
