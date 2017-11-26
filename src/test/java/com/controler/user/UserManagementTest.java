package com.controler.user;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Date;

import org.junit.Test;
import org.mockito.Mockito;

import com.controler.utils.DateUtils;
import com.model.entities.User;
import com.model.other.State;
import com.model.other.State.Status;

public class UserManagementTest {

	@Test
	public void testUserCreationOK() {
		UserManagement userManagement = UserManagement.getInstance();

		String firstName = "Rayan";
		String lastName = "Fadhlaoui";
		Date birthdate = DateUtils.parse("19/09/1989");
		String address = "16 B Avenue Albert 1ER 94210";
		String phoneNumber = "0664197893";
		State state = userManagement.createUser(firstName, lastName, birthdate, address, phoneNumber);

		assertEquals(Status.OK, state.getStatus());

		User user = userManagement.getAllUsers().get(0);
		assertEquals(firstName, user.getFirstName());
		assertEquals(lastName, user.getLastName());
		assertEquals(birthdate, user.getBirthdate());
		assertEquals(address, user.getAddress());
		assertEquals(phoneNumber, user.getPhoneNumber());
	}

	@Test
	public void testUserCreationWithErrors() {
		UserManagement userManagement = UserManagement.getInstance();

		State state = userManagement.createUser(null, null, null, null, null);
		assertEquals(Status.KO, state.getStatus());
		assertEquals("Missing fields: (First name, Last name, Birthdate, Address, Phone number )", state.getMessages().get(0));
	}

	@Test
	public void testUserCreationWithIncompletePhoneNumber() {
		UserManagement userManagement = UserManagement.getInstance();

		String firstName = "Rayan";
		String lastName = "Fadhlaoui";
		Date birthdate = DateUtils.parse("19/09/1989");
		String address = "16 B Avenue Albert 1ER 94210";
		State state9Numbers = userManagement.createUser(firstName, lastName, birthdate, address, "+011150459");
		State stateSpecialCharac = userManagement.createUser(firstName, lastName, birthdate, address, "0-11150459");
		State stateLetters = userManagement.createUser(firstName, lastName, birthdate, address, "01457664te");

		assertEquals(Status.KO, state9Numbers.getStatus());
		assertEquals("Invalid phone number", state9Numbers.getMessages().get(0));

		assertEquals(Status.KO, stateSpecialCharac.getStatus());
		assertEquals("Invalid phone number", stateSpecialCharac.getMessages().get(0));

		assertEquals(Status.KO, stateLetters.getStatus());
		assertEquals("Invalid phone number", stateLetters.getMessages().get(0));
	}

	@Test
	public void testUserUpdateOK() {
		UserManagement userManagement = UserManagement.getInstance();

		User user = createAndAddUser("Rayan", "Fadhlaoui", DateUtils.parse("19/09/1989"), userManagement);

		UserUpdater userUpdater = userManagement.getUserUpdater(user);
		userUpdater.setFirstName("Jean");
		userUpdater.setLastName("Dupont");
		userUpdater.setBirthdate(DateUtils.parse("19/08/1989"));
		userUpdater.setAddress("3 Avenue Albert 94430");
		userUpdater.setPhoneNumber("0145766419");

		State state = userManagement.updateUser(userUpdater);

		assertEquals(Status.OK, state.getStatus());

		user = userManagement.getAllUsers().get(0);
		assertEquals("Jean", user.getFirstName());
		assertEquals("Dupont", user.getLastName());
		assertEquals(DateUtils.parse("19/08/1989"), user.getBirthdate());
		assertEquals("3 Avenue Albert 94430", user.getAddress());
		assertEquals("0145766419", user.getPhoneNumber());

	}

	@Test
	public void testUserUpdateWithMultipleError() {
		UserManagement userManagement = UserManagement.getInstance();

		User user = createAndAddUser("Rayan", "Fadhlaoui", DateUtils.parse("19/09/1989"), userManagement);

		UserUpdater userUpdater = userManagement.getUserUpdater(user);
		userUpdater.setFirstName("");
		userUpdater.setLastName("Dupont");
		userUpdater.setBirthdate(DateUtils.parse("19/08/1989"));
		userUpdater.setAddress("3 Avenue Albert 94430");
		userUpdater.setPhoneNumber("014766419");

		State state = userManagement.updateUser(userUpdater);

		assertEquals(Status.KO, state.getStatus());
		assertEquals("Missing fields: (First name )", state.getMessages().get(0));
		assertEquals("Invalid phone number", state.getMessages().get(1));

	}

	@Test
	public void testDeleteUser() {
		UserManagement userManagement = UserManagement.getInstance();

		User rayanUser = createAndAddUser("Rayan", "Fadhlaoui", DateUtils.parse("19/09/1989"), userManagement);

		State state = userManagement.deleteUser(rayanUser);
		assertEquals(Status.OK, state.getStatus());
		assertEquals(0, userManagement.getAllUsers().size());

		state = userManagement.deleteUser(rayanUser);
		assertEquals(Status.KO, state.getStatus());
		assertEquals("User does not exist", state.getMessages().get(0));
	}

	@Test
	public void testFindAndDisplayUser() {
		mockLoginGenerator("AB12345678");

		UserManagement userManagement = UserManagement.getInstance();

		User rayanUser = createAndAddUser("Rayan", "Fadhlaoui", DateUtils.parse("19/09/1989"), userManagement);
		User user = userManagement.findUser(rayanUser.getLogin());
		
		assertEquals(rayanUser.getFirstName(), user.getFirstName());
		assertEquals(rayanUser.getLastName(), user.getLastName());
		assertEquals(rayanUser.getBirthdate(), user.getBirthdate());
		assertEquals(rayanUser.getAddress(), user.getAddress());
		assertEquals(rayanUser.getPhoneNumber(), user.getPhoneNumber());
		String expectedUserDisplay = "Login: AB12345678\n" + 
				"First name : Rayan\n" + 
				"Last name : Fadhlaoui\n" + 
				"Birthdate : 19/09/1989\n" + 
				"Adrress: 16 B Avenue Albert 1ER 94210\n" + 
				"Phone number : 0664197893";
		assertEquals(expectedUserDisplay, user.toString());
	}

	private User createAndAddUser(String firstName, String lastName, Date birthdate, UserManagement userManagement) {
		String address = "16 B Avenue Albert 1ER 94210";
		String phoneNumber = "0664197893";
		userManagement.createUser(firstName, lastName, birthdate, address, phoneNumber);

		User user = userManagement.getAllUsers().get(0);
		return user;
	}
	
	private void mockLoginGenerator(String login) {
		Generator loginGeneratorMock = Mockito.mock(Generator.class);

		Mockito.when(loginGeneratorMock.generateLogin()).thenReturn(login);

		Field field;
		try {
			field = Generator.class.getDeclaredField("INSTANCE");
			field.setAccessible(true);
			field.set(null, loginGeneratorMock);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.getStackTrace();
		}
	}
}
