package it.user;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Before;
import org.junit.Test;

@Setup
@Verify("EditUserProfileTest-verify.xml")
@BaseUrl("http://localhost:8080/signboard")
public class EditUserProfileTest extends ITBase {

	@Before
	public void before() {
		login("email02", "qqqq");
	}

	@Test
	@Verify
	public void fn_EditProfile() {
		clickLinkWithText("Edit profile");
		verifyTitle("Edit my profile");
		assertTextFieldEquals("profile:firstName", "firstName02");
		assertTextFieldEquals("profile:lastName", "lastName02");
		assertTextFieldEquals("profile:phone", "phone02");
		assertTextFieldEquals("profile:skype", "skype02");
		setTextField("profile:firstName", "firstName02a");
		setTextField("profile:lastName", "lastName02b");
		setTextField("profile:phone", "phone02c");
		setTextField("profile:skype", "skype02d");
		clickButton("profile:save");
		verifyAction("Profile updated.");
		verifyTitle("My dashboard");
		assertTextPresent("firstName02a lastName02b");
		assertTextPresent("email02");
		assertTextPresent("phone02c");
		assertTextPresent("skype02d");
	}

	@Test
	@Verify
	public void fn_ChangePassword() {
		clickLinkWithText("Edit profile");
		verifyTitle("Edit my profile");
		assertTextFieldEquals("passwordChange:password", "");
		assertTextFieldEquals("passwordChange:confirmPassword", "");
		setTextField("passwordChange:password", "wwww");
		setTextField("passwordChange:confirmPassword", "wwww");
		clickButton("passwordChange:changePassword");
		verifyAction("Password changed.");
		clickLinkWithText("Log out");
		login("email02", "wwww");
		assertTextPresent("firstName02 lastName02");
		assertLinkPresentWithText("Log out");
	}

	@Test
	public void nav() {
		clickLinkWithText("Edit profile");
		clickLink("profile:cancel");
		verifyTitle("Welcome");

		clickLinkWithText("Edit profile");
		clickLink("passwordChange:cancel");
		verifyTitle("Welcome");
	}

}
