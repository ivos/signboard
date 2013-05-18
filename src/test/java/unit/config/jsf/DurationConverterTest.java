package unit.config.jsf;

import static org.junit.Assert.*;

import javax.faces.application.FacesMessage;
import javax.faces.convert.ConverterException;

import net.sf.seaf.test.jmock.JMockSupport;

import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

import com.github.ivos.signboard.config.jsf.DurationConverter;
import com.github.ivos.signboard.config.jsf.ViewContext;

public class DurationConverterTest extends JMockSupport {

	DurationConverter c;
	ViewContext viewContext;

	@Before
	public void before() {
		c = new DurationConverter();
		viewContext = mock(ViewContext.class);
		c.setViewContext(viewContext);
	}

	@Test
	public void toString_() {
		assertEquals("", c.toString(null, null));
		assertEquals("0:00", c.toString(null, 0L));
		assertEquals("0:00", c.toString(null, 1000L * 30 - 1));
		assertEquals("0:00", c.toString(null, 1000L * 30));
		assertEquals("0:01", c.toString(null, getDuration(0, 1)));
		assertEquals("0:09", c.toString(null, getDuration(0, 9)));
		assertEquals("0:10", c.toString(null, getDuration(0, 10)));
		assertEquals("0:11", c.toString(null, getDuration(0, 11)));
		assertEquals("0:59", c.toString(null, getDuration(0, 59)));
		assertEquals("1:00", c.toString(null, getDuration(1, 0)));
		assertEquals("1:01", c.toString(null, getDuration(1, 1)));
		assertEquals("1:59", c.toString(null, getDuration(1, 59)));
		assertEquals("2:00", c.toString(null, getDuration(2, 0)));
		assertEquals("9:59", c.toString(null, getDuration(9, 59)));
		assertEquals("10:00", c.toString(null, getDuration(10, 0)));
		assertEquals("10:01", c.toString(null, getDuration(10, 1)));
		assertEquals("23:59", c.toString(null, getDuration(23, 59)));
		assertEquals("0:00", c.toString(null, getDuration(24, 0)));
	}

	private Long getDuration(int hour, int minute) {
		return 1000L * 60 * (minute + (60 * hour));
	}

	@Test
	public void toObject_Ok() {
		// empty
		assertNull(c.toObject(null, null));
		assertNull(c.toObject(null, ""));
		// zero
		assertEquals(getDuration(0, 0), c.toObject(null, "0"));
		assertEquals(getDuration(0, 0), c.toObject(null, "0."));
		assertEquals(getDuration(0, 0), c.toObject(null, ".0"));
		assertEquals(getDuration(0, 0), c.toObject(null, "0,"));
		assertEquals(getDuration(0, 0), c.toObject(null, ",0"));
		assertEquals(getDuration(0, 0), c.toObject(null, "0:"));
		assertEquals(getDuration(0, 0), c.toObject(null, ":0"));
		assertEquals(getDuration(0, 0), c.toObject(null, "0 "));
		assertEquals(getDuration(0, 0), c.toObject(null, " 0"));
		// full hours
		assertEquals(getDuration(1, 0), c.toObject(null, "1"));
		assertEquals(getDuration(7, 0), c.toObject(null, "7"));
		assertEquals(getDuration(23, 0), c.toObject(null, "23"));
		// minutes only
		assertEquals(getDuration(0, 1), c.toObject(null, ":1"));
		assertEquals(getDuration(0, 1), c.toObject(null, " 1"));
		assertEquals(getDuration(0, 8), c.toObject(null, ":8"));
		assertEquals(getDuration(0, 8), c.toObject(null, " 8"));
		assertEquals(getDuration(0, 59), c.toObject(null, ":59"));
		assertEquals(getDuration(0, 59), c.toObject(null, " 59"));
		// hours with minutes
		assertEquals(getDuration(3, 45), c.toObject(null, "3:45"));
		assertEquals(getDuration(4, 56), c.toObject(null, "4 56"));
		assertEquals(getDuration(12, 34), c.toObject(null, "12:34"));
		assertEquals(getDuration(23, 59), c.toObject(null, "23 59"));
		// decimal hours
		assertEquals(getDuration(1, 30), c.toObject(null, "1.5"));
		assertEquals(getDuration(1, 30), c.toObject(null, "1,5"));
		assertEquals(getDuration(9, 15), c.toObject(null, "9.25"));
		assertEquals(getDuration(9, 15), c.toObject(null, "9,25"));
		assertEquals(getDuration(10, 45), c.toObject(null, "10.75"));
		assertEquals(getDuration(10, 45), c.toObject(null, "10,75"));
		assertEquals(getDuration(21, 12), c.toObject(null, "21.2"));
		assertEquals(getDuration(21, 18), c.toObject(null, "21,3"));
		assertEquals(getDuration(22, 48), c.toObject(null, "22.8"));
		assertEquals(getDuration(23, 54), c.toObject(null, "23,9"));
	}

	@Test
	public void toObject_Invalid() {
		final FacesMessage msg = new FacesMessage();

		check(new Expectations() {
			{
				one(viewContext).createError("invalid.duration");
				will(returnValue(msg));
			}
		});

		try {
			c.toObject(null, "abc");
			fail("Should throw");
		} catch (ConverterException ce) {
			assertSame(msg, ce.getFacesMessage());
		}
	}

	@Test
	public void toObject_Exceeds() {
		final FacesMessage msg = new FacesMessage();

		check(new Expectations() {
			{
				one(viewContext).createError("duration.exceeds.limit");
				will(returnValue(msg));
			}
		});

		try {
			c.toObject(null, "24:00");
			fail("Should throw");
		} catch (ConverterException ce) {
			assertSame(msg, ce.getFacesMessage());
		}
	}

}
