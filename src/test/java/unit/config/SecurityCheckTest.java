package unit.config;

import static org.junit.Assert.*;
import net.sf.seaf.test.jmock.JMockSupport;

import org.junit.Test;

import com.github.ivos.signboard.config.security.SecurityCheck;

public class SecurityCheckTest extends JMockSupport {

	@Test
	public void getIdFromRequestURI() {
		SecurityCheck sc = new SecurityCheck();

		assertEquals("code1",
				sc.getIdFromRequestURI("/signboard/project/code1/edit"));
		assertEquals("signboard", sc.getIdFromRequestURI("/signboard/"));
		assertEquals("", sc.getIdFromRequestURI("/signboard"));
		assertEquals("", sc.getIdFromRequestURI("/"));
		assertEquals("", sc.getIdFromRequestURI(""));
	}

}
