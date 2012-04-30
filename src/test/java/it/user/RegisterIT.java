package it.user;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import net.sf.lightair.LightAir;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(LightAir.class)
@Setup
@Verify
@BaseUrl("http://localhost:8080/signboard")
public class RegisterIT {

	@Test
	public void fc_Main() {
		register("email1", "password1", "firstName1", "lastName1", "phone1",
				"skype1");
	}

	private void register(String email, String password, String firstName,
			String lastName, String phone, String skype) {
		beginAt("register");
		setTextField("main:email", email);
		setTextField("main:password", password);
		setTextField("main:confirmPassword", password);
		setTextField("main:firstName", firstName);
		setTextField("main:lastName", lastName);
		setTextField("main:phone", phone);
		setTextField("main:skype", skype);
		submit();
	}

}
