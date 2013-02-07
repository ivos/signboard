package unit.project;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.github.ivos.signboard.project.model.Project;
import com.github.ivos.signboard.project.model.ProjectMember;
import com.github.ivos.signboard.project.model.ProjectMemberRole;
import com.github.ivos.signboard.project.model.ProjectMemberStatus;
import com.github.ivos.signboard.user.model.User;

public class ProjectIsActiveAdministratorTest {

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

		ProjectMember pm1 = new ProjectMember(p1, u1,
				ProjectMemberStatus.disabled);
		p1.getProjectMembers().add(pm1);
		u1.getProjectMembers().add(pm1);
		pm1.getRoles().add(ProjectMemberRole.admin);
		pm1.getRoles().add(ProjectMemberRole.user);

		ProjectMember pm2 = new ProjectMember(p2, u1,
				ProjectMemberStatus.active);
		p2.getProjectMembers().add(pm2);
		u1.getProjectMembers().add(pm2);
		pm2.getRoles().add(ProjectMemberRole.user);

		ProjectMember pm3 = new ProjectMember(p3, u1,
				ProjectMemberStatus.active);
		p3.getProjectMembers().add(pm3);
		u1.getProjectMembers().add(pm3);
		pm3.getRoles().add(ProjectMemberRole.admin);

		ProjectMember pm4 = new ProjectMember(p4, u2,
				ProjectMemberStatus.active);
		p4.getProjectMembers().add(pm4);
		u2.getProjectMembers().add(pm4);
		pm4.getRoles().add(ProjectMemberRole.admin);
		pm4.getRoles().add(ProjectMemberRole.user);
	}

	@Test
	public void isMember() {
		assertFalse(p1.isActiveAdministrator(u1));
		assertFalse(p2.isActiveAdministrator(u1));
		assertTrue(p3.isActiveAdministrator(u1));
		assertTrue(p4.isActiveAdministrator(u2));

		assertFalse(p1.isActiveAdministrator(u2));
		assertFalse(p2.isActiveAdministrator(u2));
		assertFalse(p3.isActiveAdministrator(u2));
		assertFalse(p4.isActiveAdministrator(u1));
	}

}
