package it.user;

import static junit.framework.Assert.*;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import net.sf.lightair.LightAir;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(LightAir.class)
@Setup
@Verify("RegisterIT.xml")
@BaseUrl("http://localhost:8080/signboard")
public class RegisterIT {

	@Test
	@Verify
	public void fn() {
		register("email1", "password1", "password1", "firstName1", "lastName1",
				"phone1", "skype1");
		assertTextPresent("Saved.");
		register("email2", "password2", "password2", "firstName2", "lastName2",
				"phone2", "skype2");
		assertTextPresent("Saved.");
	}

	private void register(String email, String password,
			String confirmPassword, String firstName, String lastName,
			String phone, String skype) {
		beginAt("register");
		setTextField("main:email", email);
		setTextField("main:password", password);
		setTextField("main:confirmPassword", confirmPassword);
		setTextField("main:firstName", firstName);
		setTextField("main:lastName", lastName);
		setTextField("main:phone", phone);
		setTextField("main:skype", skype);
		submit();
	}

	@Test
	public void val_MandatoryFields() {
		register("", "password1", "password1", "firstName1", "lastName1",
				"phone1", "skype1");
		assertTextPresent("May not be empty.");
		register("email1", "", "password1", "firstName1", "lastName1",
				"phone1", "skype1");
		assertTextNotPresent("Saved.");
		register("email1", "password1", "", "firstName1", "lastName1",
				"phone1", "skype1");
		assertTextNotPresent("Saved.");
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
		assertTextNotPresent("Saved.");
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
		assertTextPresent("Saved.");
		register("email1", "password2", "password2", "firstName2", "lastName2",
				"phone2", "skype2");
		assertTextPresent("This e-mail address is already registered.");
	}

	@Test
	public void nav() {
		beginAt("/");
		assertTitleEquals("Welcome - Signboard");
		assertTrue(getTestingEngine().getPageURL().toString()
				.endsWith("/signboard/"));
		clickLinkWithExactText("register");
		assertTitleEquals("Register - Signboard");
		assertTrue(getTestingEngine().getPageURL().toString()
				.contains("/signboard/register"));
		assertTextPresent("Please enter your registration.");
		clickLinkWithExactText("Cancel");
		assertTitleEquals("Welcome - Signboard");
		assertTrue(getTestingEngine().getPageURL().toString()
				.endsWith("/signboard/"));
	}

}
