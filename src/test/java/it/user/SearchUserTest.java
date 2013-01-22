package it.user;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Before;
import org.junit.Test;

@Setup
@Verify
@BaseUrl("http://localhost:8080/signboard")
public class SearchUserTest extends ITBase {

	@Before
	public void before() {
		login("email01", "qqqq");
	}

	private void search(String lastName, String firstName, String email,
			String phone, String status) {
		setTextField("search:lastName", lastName);
		setTextField("search:firstName", firstName);
		setTextField("search:email", email);
		setTextField("search:phone", phone);
		selectOption("search:status", status);
		clickButton("search:search");
	}

	@Test
	public void fn_Paging_NoSearch() {
		gotoPage("user");
		search("", "", "", "", "");
		assertTextPresent("1 .. 10 of 11");
		assertTextPresent("phone01");
		assertTextPresent("phone02");
		assertTextPresent("phone10");
		clickLinkWithExactText("2");
		assertTextPresent("11 .. 11 of 11");
		assertTextPresent("phone11");
	}

	@Test
	public void fn_SearchByNames() {
		gotoPage("user");
		search("1", "", "", "", "");
		assertTextPresent("phone01");
		assertTextPresent("phone10");
		assertTextPresent("phone11");

		search("", "0", "", "", "");
		assertTextPresent("phone01");
		assertTextPresent("phone02");
		assertTextPresent("phone09");
		assertTextPresent("phone10");

		search("1", "0", "", "", "");
		assertTextPresent("phone01");
		assertTextPresent("phone10");

		clickButton("search:reset");
		assertTextPresent("1 .. 10 of 11");
	}

	@Test
	public void fn_SearchByStatus() {
		gotoPage("user");
		search("", "", "", "", "Active");
		assertTextPresent("phone01");
		assertTextPresent("phone02");
		assertTextPresent("phone10");
		assertTextPresent("phone11");
		assertTextNotPresent("phone05");
		assertTextNotPresent("phone07");
		assertTextNotPresent("phone09");

		search("", "", "", "", "Disabled");
		assertTextPresent("phone05");
		assertTextPresent("phone07");
		assertTextPresent("phone09");
		assertTextNotPresent("phone01");
		assertTextNotPresent("phone02");
		assertTextNotPresent("phone10");
		assertTextNotPresent("phone11");

		clickButton("search:reset");
		assertTextPresent("1 .. 10 of 11");
	}

	@Test
	public void fn_Sort() {
		gotoPage("user");
		selectOption("search:sort", "By recent registration");
		search("1", "", "", "", "");
		assertTableEquals("search:userListBeanPageItems",
				new String[][] {
						{ "Last name", "First name", "E-mail", "Phone",
								"Status" },
						{ "lastName11", "firstName11", "email11", "phone11",
								"Active" },
						{ "lastName10", "firstName10", "email10", "phone10",
								"Active" },
						{ "lastName01", "firstName01", "email01", "phone01",
								"Active" } });

		selectOption("search:sort", "By recent login");
		search("", "", "", "", "Disabled");
		assertTableEquals("search:userListBeanPageItems",
				new String[][] {
						{ "Last name", "First name", "E-mail", "Phone",
								"Status" },
						{ "lastName09", "firstName09", "email09", "phone09",
								"Disabled" },
						{ "lastName07", "firstName07", "email07", "phone07",
								"Disabled" },
						{ "lastName05", "firstName05", "email05", "phone05",
								"Disabled" } });
	}

	@Test
	public void fn_Columns() {
		gotoPage("user");
		search("1", "", "", "", "");
		assertTableEquals("search:userListBeanPageItems",
				new String[][] {
						{ "Last name", "First name", "E-mail", "Phone",
								"Status" },
						{ "lastName01", "firstName01", "email01", "phone01",
								"Active" },
						{ "lastName10", "firstName10", "email10", "phone10",
								"Active" },
						{ "lastName11", "firstName11", "email11", "phone11",
								"Active" } });
	}

	@Test
	public void sec_DeniedForNonSystemAdministrator() {
		login("email02", "qqqq");
		gotoPage("user");
		assertAccessDenied();
	}

}
