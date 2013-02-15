package it.project;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Before;
import org.junit.Test;

@Setup({ "../deleteAll.xml", "../users.xml", "EditProjectTest.xml" })
@Verify
@BaseUrl("http://localhost:8080/signboard")
public class EditProjectTest extends ITBase {

	@Before
	public void before() {
		login("email1", "qqqq");
	}

	private void edit(String number) {
		gotoPage("project/code" + number + "/edit");
		verifyTitle("Edit project");
		assertTextFieldEquals("edit:name", "name" + number);
		assertTextFieldEquals("edit:description", "description" + number);
		setTextField("edit:name", "name" + number + "a");
		setTextField("edit:description", "description" + number + "b");
		clickButton("edit:save");
		verifyAction("Saved.");
		verifyTitle("View project");
	}

	@Test
	public void fn() {
		edit("1");
	}

	@Test
	public void fn_OtherUser() {
		login("email3", "qqqq");
		edit("2");
	}

	@Test
	@Verify("EditProjectTest.xml")
	public void nav_Cancel() {
		gotoPage("project");
		clickLinkWithText("code1");
		verifyTitle("View project");
		clickLinkWithExactText("Edit");
		verifyTitle("Edit project");
		clickLinkWithExactText("Cancel");
		verifyTitle("View project");
	}

	@Test
	@Verify("EditProjectTest.xml")
	public void sec_MustBeProjectMember() {
		gotoPage("project/code2/edit");
		assertAccessDenied();
	}

	@Test
	@Verify("EditProjectTest.xml")
	public void sec_MustBeProjectAdministrator() {
		gotoPage("project/code3/edit");
		assertAccessDenied();
	}

	@Test
	@Verify("EditProjectTest.xml")
	public void sec_MustBeActiveProjectAdministrator() {
		gotoPage("project/code4/edit");
		assertAccessDenied();
	}

	@Test
	@Verify("EditProjectTest.xml")
	public void sec_HideEditButtonForNonProjectMember() {
		gotoPage("project/code2");
		assertLinkNotPresent("view:edit");
	}

	@Test
	@Verify("EditProjectTest.xml")
	public void sec_HideEditButtonForNonProjectAdministrator() {
		gotoPage("project/code3");
		assertLinkNotPresent("view:edit");
	}

	@Test
	@Verify("EditProjectTest.xml")
	public void sec_HideEditButtonForNonActiveProjectAdministrator() {
		gotoPage("project/code4");
		assertLinkNotPresent("view:edit");
	}

}
