package it.user;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import static org.junit.Assert.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Test;

@Setup({ "../deleteAll.xml", "UserDashboardTest.xml" })
@Verify
@BaseUrl("http://localhost:8080/signboard")
public class UserDashboardTest extends ITBase {

	@Test
	public void fn() {
		login("email02", "qqqq");
		verifyTitle("My dashboard");

		assertTextPresent("Dashboard of firstName02 lastName02");
		assertTextPresent("email02");
		assertTextPresent("phone02");
		assertTextPresent("S skype02");
		assertElementPresentByXPath("//div[@id='main-section']//i[@class='icon-phone']");

		assertLinkPresentWithExactText("projectName02");
		assertLinkPresentWithExactText("projectName04");
		assertLinkPresentWithExactText("projectName05");
		assertEquals("Projects count", 3,
				getElementsByXPath("//div[@id='my-projects']/ul/li").size());
	}

	@Test
	public void fn_OtherUser() {
		login("email01", "qqqq");
		verifyTitle("My dashboard");

		assertTextPresent("Dashboard of firstName01 lastName01");
		assertTextPresent("email01");
		assertTextPresent("phone01");
		assertTextPresent("S skype01");
		assertElementPresentByXPath("//div[@id='main-section']//i[@class='icon-phone']");

		assertLinkPresentWithExactText("projectName03");
		assertLinkPresentWithExactText("projectName04");
		assertEquals("Projects count", 2,
				getElementsByXPath("//div[@id='my-projects']/ul/li").size());
	}

	@Test
	public void fn_OptionalFields() {
		login("email03", "qqqq");
		verifyTitle("My dashboard");

		assertTextPresent("Dashboard of firstName03 lastName03");
		assertTextPresent("email03");
		assertElementNotPresentByXPath("//div[@id='main-section']//i[@class='icon-phone']");

		assertTextPresent("My projects");
		assertTextPresent("You are not a member of any project yet.");
		assertLinkPresent("find-project");
		assertLinkPresent("create-project");
	}

	@Test
	public void nav_EditProfile() {
		login("email02", "qqqq");
		clickLink("change-profile");
		verifyTitle("Edit my profile");
	}

	@Test
	public void nav_FindProject() {
		login("email03", "qqqq");
		clickLink("find-project");
		verifyTitle("Search projects");
	}

	@Test
	public void nav_CreateProject() {
		login("email03", "qqqq");
		clickLink("create-project");
		verifyTitle("Create project");
	}

	@Test
	public void nav_ViewProject() {
		login("email02", "qqqq");
		clickLinkWithExactText("projectName05");
		verifyTitle("View project");
		assertTextPresent("projectCode05");
	}

}
