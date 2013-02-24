package unit.config;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

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
	HttpServletRequest request;

	@Before
	public void before() {
		sc = new SecurityCheck();
		log = mock(Logger.class);
		sc.setLog(log);
		entityManager = mock(EntityManager.class);
		sc.setEntityManager(entityManager);
		projectMember = new ProjectMember(new Project("code1"), new User(),
				null);
		request = mock(HttpServletRequest.class);
		sc.setRequest(request);

		check(new Expectations() {
			{
				allowing(log);
			}
		});
	}

	@Test
	public void getProjectIdFromRequestURI_Project_Edit_RootWar() {
		check(new Expectations() {
			{
				one(request).getRequestURI();
				will(returnValue("/project/code1/edit"));

				one(request).getContextPath();
				will(returnValue(""));
			}
		});

		assertEquals("code1", sc.getProjectIdFromRequestURI());
	}

	@Test
	public void getProjectIdFromRequestURI_Project_Edit() {
		check(new Expectations() {
			{
				one(request).getRequestURI();
				will(returnValue("/signboard/project/code1/edit"));

				one(request).getContextPath();
				will(returnValue("/signboard"));
			}
		});

		assertEquals("code1", sc.getProjectIdFromRequestURI());
	}

	@Test
	public void getProjectIdFromRequestURI_Project_View() {
		check(new Expectations() {
			{
				one(request).getRequestURI();
				will(returnValue("/signboard/project/code1"));

				one(request).getContextPath();
				will(returnValue("/signboard"));
			}
		});

		assertEquals("code1", sc.getProjectIdFromRequestURI());
	}

	@Test
	public void getProjectIdFromRequestURI_Project_Members() {
		check(new Expectations() {
			{
				one(request).getRequestURI();
				will(returnValue("/signboard/project/code1/member"));

				one(request).getContextPath();
				will(returnValue("/signboard"));
			}
		});

		assertEquals("code1", sc.getProjectIdFromRequestURI());
	}

	@Test
	public void getProjectIdFromRequestURI_Project_Members_Page() {
		check(new Expectations() {
			{
				one(request).getRequestURI();
				will(returnValue("/signboard/project/code1/member/page/2"));

				one(request).getContextPath();
				will(returnValue("/signboard"));
			}
		});

		assertEquals("code1", sc.getProjectIdFromRequestURI());
	}

	//

	@Test
	public void getProjectIdFromRequestURI_ProjectMember() {
		check(new Expectations() {
			{
				one(request).getRequestURI();
				will(returnValue("/signboard/projectMember/123"));

				one(request).getContextPath();
				will(returnValue("/signboard"));

				one(entityManager).find(ProjectMember.class, 123L);
				will(returnValue(projectMember));
			}
		});

		assertEquals("code1", sc.getProjectIdFromRequestURI());
	}

	@Test
	public void getProjectIdFromRequestURI_UnknownDomain() {
		check(new Expectations() {
			{
				one(request).getRequestURI();
				will(returnValue("signboard/unknowndomain/code1"));

				one(request).getContextPath();
				will(returnValue("/signboard"));
			}
		});

		try {
			sc.getProjectIdFromRequestURI();
			fail("Should throw");
		} catch (RuntimeException e) {
			assertEquals("Project id cannot be extracted "
					+ "from request URI signboard/unknowndomain/code1",
					e.getMessage());
		}
	}

	@Test
	public void getProjectIdFromRequestURI_NoDomainWithSlash() {
		check(new Expectations() {
			{
				one(request).getRequestURI();
				will(returnValue("/signboard/"));

				one(request).getContextPath();
				will(returnValue("/signboard"));
			}
		});

		try {
			sc.getProjectIdFromRequestURI();
			fail("Should throw");
		} catch (RuntimeException e) {
			assertEquals(
					"Project id cannot be extracted from request URI /signboard/",
					e.getMessage());
		}
	}

	@Test
	public void getProjectIdFromRequestURI_NoDomainNoSlash() {
		check(new Expectations() {
			{
				one(request).getRequestURI();
				will(returnValue("/signboard"));

				one(request).getContextPath();
				will(returnValue("/signboard"));
			}
		});

		try {
			sc.getProjectIdFromRequestURI();
			fail("Should throw");
		} catch (RuntimeException e) {
			assertEquals(
					"Project id cannot be extracted from request URI /signboard",
					e.getMessage());
		}
	}

	@Test
	public void getProjectIdFromRequestURI_Slash() {
		check(new Expectations() {
			{
				one(request).getRequestURI();
				will(returnValue("/"));

				one(request).getContextPath();
				will(returnValue(""));
			}
		});

		try {
			sc.getProjectIdFromRequestURI();
			fail("Should throw");
		} catch (RuntimeException e) {
			assertEquals("Project id cannot be extracted from request URI /",
					e.getMessage());
		}
	}

	@Test
	public void getProjectIdFromRequestURI_Empty() {
		check(new Expectations() {
			{
				one(request).getRequestURI();
				will(returnValue(""));

				one(request).getContextPath();
				will(returnValue(""));
			}
		});

		try {
			sc.getProjectIdFromRequestURI();
			fail("Should throw");
		} catch (RuntimeException e) {
			assertEquals("Project id cannot be extracted from request URI ",
					e.getMessage());
		}
	}

}
