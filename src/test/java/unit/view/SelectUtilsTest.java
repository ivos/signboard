package unit.view;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import net.sf.seaf.test.jmock.JMockSupport;

import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

import com.github.ivos.signboard.config.jsf.SelectUtils;
import com.github.ivos.signboard.config.jsf.ViewContext;

public class SelectUtilsTest extends JMockSupport {

	SelectUtils u;

	@Before
	public void before() {
		u = new SelectUtils();
	}

	@Test
	public void convertToSelectItems() {
		final ViewContext viewContext = mock(ViewContext.class);
		u.setViewContext(viewContext);

		check(new Expectations() {
			{
				one(viewContext).getLabel("enum_SomeEnum_value1");
				will(returnValue("label1"));

				one(viewContext).getLabel("enum_SomeEnum_value2");
				will(returnValue("label2"));

				one(viewContext).getLabel("enum_SomeEnum_value3");
				will(returnValue("label3"));
			}
		});

		List<SelectItem> items = u.convertToSelectItems(SomeEnum.class);

		assertEquals(3, items.size());
		assertEquals(SomeEnum.value1, items.get(0).getValue());
		assertEquals("label1", items.get(0).getLabel());
		assertEquals(SomeEnum.value2, items.get(1).getValue());
		assertEquals("label2", items.get(1).getLabel());
		assertEquals(SomeEnum.value3, items.get(2).getValue());
		assertEquals("label3", items.get(2).getLabel());
	}

	@Test
	public void convertToStrings() {
		Collection<SomeEnum> c = new ArrayList<SomeEnum>();
		c.add(SomeEnum.value2);
		c.add(SomeEnum.value3);

		assertArrayEquals(new String[] { "value2", "value3" }, u
				.convertToStrings(c).toArray(new String[2]));
	}

	@Test
	public void convertToEnumSet() {
		Set<SomeEnum> set = u.convertToEnumSet(
				Arrays.asList("value2", "value3"), SomeEnum.class);
		assertEquals(2, set.size());
		assertTrue(set.contains(SomeEnum.value2));
		assertTrue(set.contains(SomeEnum.value3));
	}

}
