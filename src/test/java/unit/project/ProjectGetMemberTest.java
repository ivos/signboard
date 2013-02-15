package unit.project;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.github.ivos.signboard.project.model.Project;
import com.github.ivos.signboard.projectmember.model.ProjectMember;
import com.github.ivos.signboard.user.model.User;

public class ProjectGetMemberTest {

	Project p1, p2, p3, p4;
	User u1, u2;
	ProjectMember pm1, pm2, pm3, pm4;

	@Before
	public void before() {
		p1 = new Project("1");
		p2 = new Project("2");
		p3 = new Project("3");
		p4 = new Project("4");
		u1 = new User("1");
		u2 = new User("2");

		pm1 = new ProjectMember(p1, u1, null);
		p1.getProjectMembers().add(pm1);
		u1.getProjectMembers().add(pm1);

		pm2 = new ProjectMember(p2, u1, null);
		p2.getProjectMembers().add(pm2);
		u1.getProjectMembers().add(pm2);

		pm3 = new ProjectMember(p3, u1, null);
		p3.getProjectMembers().add(pm3);
		u1.getProjectMembers().add(pm3);

		pm4 = new ProjectMember(p4, u2, null);
		p4.getProjectMembers().add(pm4);
		u2.getProjectMembers().add(pm4);
	}

	@Test
	public void isMember() {
		assertSame(pm1, p1.getMember(u1));
		assertSame(pm2, p2.getMember(u1));
		assertSame(pm3, p3.getMember(u1));
		assertSame(pm4, p4.getMember(u2));

		assertNull(p1.getMember(u2));
		assertNull(p2.getMember(u2));
		assertNull(p3.getMember(u2));
		assertNull(p4.getMember(u1));
	}

}
