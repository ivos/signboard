package it.project;

import static junit.framework.Assert.*;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Before;
import org.junit.Test;

@Setup
@Verify("CreateProjectTest.xml")
@BaseUrl("http://localhost:8080/signboard")
public class CreateProjectTest extends ITBase {

	@Before
	public void before() {
		login("email1", "password1");
	}

	@Test
	@Verify
	public void fn() {
		create("code1", "name1", "description1");
		verifyAction("Saved.");
		create("code2", "name2", "description2");
		verifyAction("Saved.");
	}

	@Test
	@Verify
	public void fn_OptionalFields() {
		create("code1", "name1", "");
		verifyAction("Saved.");
	}

	private void create(String code, String name, String description) {
		gotoPage("project/create");
		setTextField("edit:code", code);
		setTextField("edit:name", name);
		setTextField("edit:description", description);
		submit();
	}

	@Test
	public void val_MandatoryFields() {
		create("", "name1", "description1");
		assertTextPresent("May not be empty.");
		create("code1", "", "description1");
		assertTextPresent("May not be empty.");
	}

	@Test
	public void val_Size() {
		create("cd", "name1", "description1");
		assertTextPresent("Size must be between 3 and 64.");
	}

	@Test
	@Verify
	public void val_CodeRegex() {
		// starts with dash
		create("-bc", "name1", "description1");
		verifyCodeRegexpError();
		// invalid char
		create("ab?", "name1", "description1");
		verifyCodeRegexpError();
		// pass, verify allowed chars
		create("A-b_c09", "name1", "description1");
		verifyAction("Saved.");
	}

	private void verifyCodeRegexpError() {
		assertTextPresent("Project code must consist of English letters, digits, "
				+ "underscores and dashes and must not start with a dash.");
	}

	@Test
	public void val_CodeNotReserved() {
		create("create", "name1", "description1");
		verifyProjectCodeNotReservedError();
		create("page", "name1", "description1");
		verifyProjectCodeNotReservedError();
	}

	private void verifyProjectCodeNotReservedError() {
		assertTextPresent("Project codes 'create' and 'page' are reserved.");
	}

	@Test
	@Verify
	public void val_CodeNotRegistered() {
		create("code1", "name1", "description1");
		verifyAction("Saved.");
		create("code1", "name2", "description2");
		assertTextPresent("This project code is already registered.");
	}

	@Test
	public void nav() {
		gotoPage("/");
		assertTitleEquals("Welcome - Signboard");
		assertTrue(getTestingEngine().getPageURL().toString()
				.endsWith("/signboard/"));

		clickLinkWithExactText("Project");
		assertTitleEquals("Search projects - Signboard");
		assertTrue(getTestingEngine().getPageURL().toString()
				.contains("/signboard/project"));

		clickLinkWithExactText("Create new");
		assertTitleEquals("Create project - Signboard");
		assertTrue(getTestingEngine().getPageURL().toString()
				.contains("/signboard/project/create"));
		assertTextPresent("Create project");

		clickLinkWithExactText("Cancel");
		assertTitleEquals("Search projects - Signboard");
		assertTrue(getTestingEngine().getPageURL().toString()
				.endsWith("/signboard/project"));
	}

	@Test
	public void sec() {
		beginAt("/project/create");
		assertTitleEquals("Log in - Signboard");
		fillAndSubmitLoginForm("email1", "password1");
		gotoPage("/project/create");
		assertTitleEquals("Create project - Signboard");
	}

}
