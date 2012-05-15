package it.project;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.Test;

@Setup
@Verify
@BaseUrl("http://localhost:8080/signboard")
public class CreateProjectIT extends ITBase {

	@Test
	public void fn() {
		login("email1", "password1");
		gotoPage("project/create");
		assertTitleEquals("Create project - Signboard");
		setTextField("edit:code", "code1");
		setTextField("edit:name", "name1");
		setTextField("edit:description", "description1");
		submit();
	}

}
