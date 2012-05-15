package it;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import net.sf.lightair.LightAir;

import org.junit.Ignore;
import org.junit.runner.RunWith;

@RunWith(LightAir.class)
@Ignore
public class ITBase {

	public static void login(String email, String password) {
		beginAt("login");
		setTextField("main:email", email);
		setTextField("main:password", password);
		submit();
	}

}
