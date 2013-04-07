package it.task;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import it.ITBase;
import net.sf.lightair.annotation.BaseUrl;
import net.sf.lightair.annotation.Setup;
import net.sf.lightair.annotation.Verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Setup({ "../deleteAll.xml", "../users.xml", "../projects.xml",
		"SearchTaskTest.xml" })
@Verify("SearchTaskTest.xml")
@BaseUrl("http://localhost:8080/signboard")
public class SearchTaskTest extends ITBase {

	@Before
	public void before() {
		login("email1", "qqqq");
		turnJavaScriptOff();
		gotoPage("task");
	}

	@After
	public void after() {
		turnJavaScriptOn();
	}

	private void search(String project, String goal, String timeCreated,
			String timeCreated__To, String author, String description) {
		selectOption("search:project", chooseOne(project));
		setTextField("search:goal", goal);
		setTextField("search:timeCreated", timeCreated);
		setTextField("search:timeCreated__To", timeCreated__To);
		selectOption("search:author", chooseOne(author));
		setTextField("search:description", description);
		clickButton("search:search");
	}

	@Test
	public void fn_Paging_NoSearch() {
		search("", "", "", "", "", "");
		assertTextPresent("1 .. 10 of 21");
		assertTextPresent("goal01");
		assertTextPresent("goal02");
		assertTextPresent("goal10");
		clickLinkWithExactText("2");
		assertTextPresent("11 .. 20 of 21");
		assertTextPresent("goal11");
		assertTextPresent("goal12");
		assertTextPresent("goal20");
		clickLinkWithExactText("3");
		assertTextPresent("21 .. 21 of 21");
		assertTextPresent("goal21");
	}

	@Test
	public void fn_SearchByProject() {
		search("name5", "", "", "", "", "");
		assertTextPresent("goal02");
		assertTextPresent("goal04");
		assertTextPresent("goal06");
		assertTextPresent("1 .. 3 of 3");
	}

	@Test
	public void fn_SearchByGoal() {
		search("", "2", "", "", "", "");
		assertTextPresent("goal02");
		assertTextPresent("goal12");
		assertTextPresent("goal20");
		assertTextPresent("goal21");
		assertTextPresent("1 .. 4 of 4");
	}

	@Test
	public void fn_SearchByAuthor() {
		search("", "", "", "", "f2 l2, email2", "");
		assertTextPresent("goal02");
		assertTextPresent("goal10");
		assertTextPresent("goal11");
		assertTextPresent("1 .. 3 of 3");
	}

	@Test
	public void fn_SearchByDescription() {
		search("", "", "", "", "", "3");
		assertTextPresent("goal03");
		assertTextPresent("goal13");
		assertTextPresent("1 .. 2 of 2");
	}

	@Test
	public void fn_SearchByTimeCreated() {
		search("", "", "01/04/2013", "01/06/2013", "", "");
		assertTextPresent("goal04");
		assertTextPresent("goal05");
		assertTextPresent("goal06");
		assertTextPresent("goal07");
		assertTextPresent("goal08");
		assertTextPresent("1 .. 5 of 5");
	}

	@Test
	public void fn_SearchByAll() {
		search("name1", "0", "01/02/2013", "01/21/2013", "f1 l1, email1", "n0");
		assertTextPresent("goal03");
		assertTextPresent("goal05");
		assertTextPresent("goal07");
		assertTextPresent("goal08");
		assertTextPresent("goal09");
		assertTextPresent("1 .. 5 of 5");
	}

	@Test
	public void fn_Sort() {
		selectOption("search:sort", "Most recent");
		search("name5", "", "", "", "", "");
		assertTableEquals("search:taskListBeanPageItems", new String[][] {
				{ "Project", "Goal", "Created", "Author" },
				{ "name5", "goal06", "Jan 4, 2013 2:03:03 AM", "f1 l1" },
				{ "name5", "goal04", "Jan 4, 2013 2:03:01 AM", "f1 l1" },
				{ "name5", "goal02", "Jan 2, 2013 2:03:01 AM", "f2 l2" } });

		selectOption("search:sort", "Oldest");
		search("name5", "", "", "", "", "");
		assertTableEquals("search:taskListBeanPageItems", new String[][] {
				{ "Project", "Goal", "Created", "Author" },
				{ "name5", "goal02", "Jan 2, 2013 2:03:01 AM", "f2 l2" },
				{ "name5", "goal04", "Jan 4, 2013 2:03:01 AM", "f1 l1" },
				{ "name5", "goal06", "Jan 4, 2013 2:03:03 AM", "f1 l1" } });
	}

	@Test
	public void fn_Columns() {
		search("", "goal01", "", "", "", "");
		assertTableEquals("search:taskListBeanPageItems", new String[][] {
				{ "Project", "Goal", "Created", "Author" },
				{ "name1", "goal01", "Jan 1, 2013 2:03:01 AM", "f1 l1" } });
	}

	@Test
	public void val_SearchTimeCreatedDatesInterval() {
		search("", "", "01/26/2010", "01/25/2010", "", "");
		assertTextPresent("The end date must be after the start date.");
		search("", "", "01/26/2010", "01/26/2010", "", "");
		assertTextPresent("The end date must be after the start date.");
		search("", "", "01/26/2010", "01/27/2010", "", "");
		assertTextNotPresent("The end date must be after the start date.");
	}

	@Test
	public void val_TimeCreatedMustBeInPast() {
		search("", "", "01/01/2050", "", "", "");
		assertTextPresent("Must be in the past.");
		search("", "", "01/01/2010", "", "", "");
		assertTextNotPresent("Must be in the past.");
	}

}
