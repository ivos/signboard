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
			String phone, String skype) {
		setTextField("search:lastName", lastName);
		setTextField("search:firstName", firstName);
		setTextField("search:email", email);
		setTextField("search:phone", phone);
		setTextField("search:skype", skype);
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
	public void fn_Search() {
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
	public void fn_Columns() {
		gotoPage("user");
		search("1", "", "", "", "");
		assertTableEquals(
				"search:userListBeanPageItems",
				new String[][] {
						{ "Last name", "First name", "E-mail", "Phone", "Skype" },
						{ "lastName01", "firstName01", "email01", "phone01",
								"skype01" },
						{ "lastName10", "firstName10", "email10", "phone10",
								"skype10" },
						{ "lastName11", "firstName11", "email11", "phone11",
								"skype11" } });
	}

	@Test
	public void sec_DeniedForNonSystemAdministrator() {
		login("email02", "qqqq");
		gotoPage("user");
		assertAccessDenied();
	}

}
