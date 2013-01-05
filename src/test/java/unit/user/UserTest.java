package unit.user;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.ivos.signboard.user.model.User;

public class UserTest {

	@Test
	public void getFullName() throws Exception {
		User u = new User();
		u.setFirstName("firstName1");
		u.setLastName("lastName1");
		assertEquals("firstName1 lastName1", u.getFullName());
	}

}
