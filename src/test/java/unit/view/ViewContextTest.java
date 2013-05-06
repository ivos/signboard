package unit.view;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.ivos.signboard.config.jsf.ViewContext;

public class ViewContextTest {

	@Test
	public void calculateLastPage() {
		ViewContext c = new ViewContext();
		assertEquals("Count 0", 1, c.calculateLastPage(0, 10));
		assertEquals("Count 1", 1, c.calculateLastPage(1, 10));
		assertEquals("Count 9", 1, c.calculateLastPage(9, 10));
		assertEquals("Count 10", 1, c.calculateLastPage(10, 10));
		assertEquals("Count 11", 2, c.calculateLastPage(11, 10));
		assertEquals("Count 12", 2, c.calculateLastPage(12, 10));
		assertEquals("Count 19", 2, c.calculateLastPage(19, 10));
		assertEquals("Count 20", 2, c.calculateLastPage(20, 10));
		assertEquals("Count 21", 3, c.calculateLastPage(21, 10));
		assertEquals("Count 22", 3, c.calculateLastPage(22, 10));
		assertEquals("Count 29", 3, c.calculateLastPage(29, 10));
		assertEquals("Count 30", 3, c.calculateLastPage(30, 10));
		assertEquals("Count 31", 4, c.calculateLastPage(31, 10));
		assertEquals("Count 32", 4, c.calculateLastPage(32, 10));
		assertEquals("Count 1230", 123, c.calculateLastPage(1230, 10));
		assertEquals("Count 1231", 124, c.calculateLastPage(1231, 10));
	}

}
