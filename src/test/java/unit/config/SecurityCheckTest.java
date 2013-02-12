package unit.config;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;

import net.sf.seaf.test.jmock.JMockSupport;

import org.jboss.solder.logging.Logger;
import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

import com.github.ivos.signboard.config.security.SecurityCheck;
import com.github.ivos.signboard.project.model.Project;
import com.github.ivos.signboard.projectmember.model.ProjectMember;
import com.github.ivos.signboard.user.model.User;

public class SecurityCheckTest extends JMockSupport {

	SecurityCheck sc;
	Logger log;
	EntityManager entityManager;
	ProjectMember projectMember;

	@Before
	public void before() {
		sc = new SecurityCheck();
		log = mock(Logger.class);
		sc.setLog(log);
		entityManager = mock(EntityManager.class);
		sc.setEntityManager(entityManager);
		projectMember = new ProjectMember(new Project("code1"), new User(),
				null);

		check(new Expectations() {
			{
				allowing(log);
			}
		});
	}

	@Test
	public void getProjectIdFromRequestURI_Project() {
		assertEquals("code1",
				sc.getProjectIdFromRequestURI("/signboard/project/code1/edit"));
		assertEquals("code1",
				sc.getProjectIdFromRequestURI("/signboard/project/code1"));

		assertEquals(
				"code1",
				sc.getProjectIdFromRequestURI("/signboard/project/code1/member"));
		assertEquals(
				"code1",
				sc.getProjectIdFromRequestURI("/signboard/project/code1/member/page/2"));
	}

	@Test
	public void getProjectIdFromRequestURI_ProjectMember() {
		check(new Expectations() {
			{
				one(entityManager).find(ProjectMember.class, 123L);
				will(returnValue(projectMember));
			}
		});

		assertEquals("code1",
				sc.getProjectIdFromRequestURI("/signboard/projectMember/123"));
	}

	@Test
	public void getProjectIdFromRequestURI_UnknownDomain() {
		try {
			sc.getProjectIdFromRequestURI("signboard/unknowndomain/code1");
			fail("Should throw");
		} catch (RuntimeException e) {
			assertEquals("Project id cannot be extracted "
					+ "from request URI signboard/unknowndomain/code1",
					e.getMessage());
		}
	}

	@Test
	public void getProjectIdFromRequestURI_NoDomainWithSlash() {
		try {
			sc.getProjectIdFromRequestURI("/signboard/");
			fail("Should throw");
		} catch (RuntimeException e) {
			assertEquals(
					"Project id cannot be extracted from request URI /signboard/",
					e.getMessage());
		}
	}

	@Test
	public void getProjectIdFromRequestURI_NoDomainNoSlash() {
		try {
			sc.getProjectIdFromRequestURI("/signboard");
			fail("Should throw");
		} catch (RuntimeException e) {
			assertEquals(
					"Project id cannot be extracted from request URI /signboard",
					e.getMessage());
		}
	}

	@Test
	public void getProjectIdFromRequestURI_Slash() {
		try {
			sc.getProjectIdFromRequestURI("/");
			fail("Should throw");
		} catch (RuntimeException e) {
			assertEquals("Project id cannot be extracted from request URI /",
					e.getMessage());
		}
	}

	@Test
	public void getProjectIdFromRequestURI_Empty() {
		try {
			sc.getProjectIdFromRequestURI("");
			fail("Should throw");
		} catch (RuntimeException e) {
			assertEquals("Project id cannot be extracted from request URI ",
					e.getMessage());
		}
	}

}
