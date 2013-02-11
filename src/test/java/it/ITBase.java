package it;

import static junit.framework.Assert.*;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import net.sf.lightair.LightAir;
import net.sourceforge.jwebunit.htmlunit.HtmlUnitTestingEngineImpl;

import org.junit.Ignore;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;

@RunWith(LightAir.class)
@Ignore
public class ITBase {

	public static void setupAjax() {
		((HtmlUnitTestingEngineImpl) getTestingEngine()).getWebClient()
				.setAjaxController(new NicelyResynchronizingAjaxController());
	}

	public static void turnJavaScriptOff() {
		setScriptingEnabled(false);
	}

	public static void turnJavaScriptOn() {
		setScriptingEnabled(true);
	}

	public static void login(String email, String password) {
		beginAt("login");
		setupAjax();
		fillAndSubmitLoginForm(email, password);
	}

	public static void fillAndSubmitLoginForm(String email, String password) {
		setTextField("main:email", email);
		setTextField("main:password", password);
		clickButton("main:login");
	}

	public static void verifyAction(String message) {
		assertTextPresent(message);
	}

	public static void verifyTitle(String title) {
		assertTitleEquals(title + " - Signboard");
	}

	public static void assertAccessDenied() {
		verifyTitle("Unauthorized access");
		assertTextPresent("You are not authorized to perform the requested operation.");
	}

	public static void verifyURL(String url) {
		assertTrue(getTestingEngine().getPageURL().toString().endsWith(url));
	}

}
