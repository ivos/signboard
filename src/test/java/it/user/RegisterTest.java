package it.user;

import static junit.framework.Assert.*;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Test;

@Setup({ "../deleteAll.xml", "RegisterTest.xml" })
@Verify("RegisterTest.xml")
@BaseUrl("http://localhost:8080/signboard")
public class RegisterTest extends ITBase {

	@Test
	@Verify
	public void fn() {
		register("email1", "password1", "password1", "firstName1", "lastName1",
				"phone1", "skype1");
		verifyAction("Saved.");
		register("email2", "password2", "password2", "firstName2", "lastName2",
				"phone2", "skype2");
		verifyAction("Saved.");
		register("email3", "password3", "password3", "firstName3", "lastName3",
				"phone3", "skype3");
		verifyAction("Saved.");
	}

	@Test
	@Verify
	public void fn_OptionalFields() {
		register("email1", "password1", "password1", "firstName1", "lastName1",
				"", "");
		verifyAction("Saved.");
	}

	private void register(String email, String password,
			String confirmPassword, String firstName, String lastName,
			String phone, String skype) {
		beginAt("register");
		setupAjax();
		setTextField("main:email", email);
		setTextField("main:password", password);
		setTextField("main:confirmPassword", confirmPassword);
		setTextField("main:firstName", firstName);
		setTextField("main:lastName", lastName);
		setTextField("main:phone", phone);
		setTextField("main:skype", skype);
		clickButton("main:register");
	}

	@Test
	public void val_MandatoryFields() {
		register("", "password1", "password1", "firstName1", "lastName1",
				"phone1", "skype1");
		assertTextPresent("May not be empty.");
		register("email1", "", "password1", "firstName1", "lastName1",
				"phone1", "skype1");
		assertTextPresent("May not be empty.");
		register("email1", "password1", "", "firstName1", "lastName1",
				"phone1", "skype1");
		assertTextPresent("May not be empty.");
		register("email1", "password1", "password1", "", "lastName1", "phone1",
				"skype1");
		assertTextPresent("May not be empty.");
		register("email1", "password1", "password1", "firstName1", "",
				"phone1", "skype1");
		assertTextPresent("May not be empty.");
	}

	@Test
	public void val_Size() {
		register("email1", "abc", "password1", "firstName1", "lastName1",
				"phone1", "skype1");
		assertTextPresent("Size must be between 4 and 100.");
		register("email1", "password1", "abc", "firstName1", "lastName1",
				"phone1", "skype1");
	}

	@Test
	public void val_PasswordsMatch() {
		register("email1", "password1", "password2", "firstName1", "lastName1",
				"phone1", "skype1");
		assertTextPresent("Passwords must match.");
	}

	@Test
	@Verify
	public void val_EmailAlreadyRegistered() {
		register("email1", "password1", "password1", "firstName1", "lastName1",
				"phone1", "skype1");
		verifyAction("Saved.");
		register("email1", "password2", "password2", "firstName2", "lastName2",
				"phone2", "skype2");
		assertTextPresent("This e-mail address is already registered.");
	}

	@Test
	public void nav() {
		beginAt("/");
		verifyTitle("Welcome");
		assertTrue(getTestingEngine().getPageURL().toString()
				.endsWith("/signboard/"));
		clickLinkWithExactText("register");
		verifyTitle("Register");
		assertTrue(getTestingEngine().getPageURL().toString()
				.contains("/signboard/register"));
		assertTextPresent("Please enter your registration.");
		clickLinkWithExactText("Cancel");
		verifyTitle("Welcome");
		assertTrue(getTestingEngine().getPageURL().toString()
				.endsWith("/signboard/"));
	}

}
