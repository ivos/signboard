package unit.project;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.github.ivos.signboard.project.model.Project;
import com.github.ivos.signboard.projectmember.model.ProjectMember;
import com.github.ivos.signboard.user.model.User;

public class ProjectIsMemberTest {

	Project p1, p2, p3, p4;
	User u1, u2;

	@Before
	public void before() {
		p1 = new Project("1");
		p2 = new Project("2");
		p3 = new Project("3");
		p4 = new Project("4");
		u1 = new User("1");
		u2 = new User("2");

		ProjectMember pm1 = new ProjectMember(p1, u1, null);
		p1.getProjectMembers().add(pm1);
		u1.getProjectMembers().add(pm1);

		ProjectMember pm2 = new ProjectMember(p2, u1, null);
		p2.getProjectMembers().add(pm2);
		u1.getProjectMembers().add(pm2);

		ProjectMember pm3 = new ProjectMember(p3, u1, null);
		p3.getProjectMembers().add(pm3);
		u1.getProjectMembers().add(pm3);

		ProjectMember pm4 = new ProjectMember(p4, u2, null);
		p4.getProjectMembers().add(pm4);
		u2.getProjectMembers().add(pm4);
	}

	@Test
	public void isMember() {
		assertTrue(p1.isMember(u1));
		assertTrue(p2.isMember(u1));
		assertTrue(p3.isMember(u1));
		assertTrue(p4.isMember(u2));

		assertFalse(p1.isMember(u2));
		assertFalse(p2.isMember(u2));
		assertFalse(p3.isMember(u2));
		assertFalse(p4.isMember(u1));
	}

}
