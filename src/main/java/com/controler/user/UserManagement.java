package com.controler.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.controler.utils.StringUtils;
import com.model.entities.User;
import com.model.other.State;
import com.model.other.State.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;;

public class UserManagement {

	private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("(0|\\\\+33|0033)[1-9][0-9]{8}");
	private static UserManagement INSTANCE;

	private Map<String, User> users;
	private Generator loginGenerator;

	private UserManagement() {
		users = new HashMap<>();
		loginGenerator = Generator.getInstance();
	}

	public static UserManagement getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new UserManagement();
		}
		return INSTANCE;
	}

	public void reset() {
		users = new HashMap<>();
	}

	public State createUser(String firstName, String lastName, Date birthdate, String address, String phoneNumber) {
		State state = dataCheck(firstName, lastName, birthdate, address, phoneNumber);
		if (Status.OK.equals(state.getStatus())) {
			User user = new User(loginGenerator.generateLogin(), firstName, lastName, birthdate, address, phoneNumber);
			users.put(user.getLogin(), user);
		}

		return state;
	}

	private State dataCheck(String firstName, String lastName, Date birthdate, String address, String phoneNumber) {
		State state = checkFieldsPresence(firstName, lastName, birthdate, address, phoneNumber);
		state = checkDataValidity(phoneNumber, state);
		return state;
	}

	public List<User> getAllUsers() {
		return new ArrayList<>(users.values());
	}

	public State updateUser(UserUpdater userUpdater) {
		User user = userUpdater.getUser();
		State state = dataCheck(user.getFirstName(), user.getLastName(), user.getBirthdate(), user.getAddress(), user.getPhoneNumber());
		if (Status.OK.equals(state.getStatus())) {
			users.put(user.getLogin(), user);
		}
		return state;
	}

	private State checkDataValidity(String phoneNumber, State state) {
		if (phoneNumber == null) {
			return state;
		}

		Matcher m = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
		if (!m.matches()) {
			state.setStatus(Status.KO);
			state.addMessage("Invalid phone number");
		}
		return state;
	}

	private State checkFieldsPresence(String firstName, String lastName, Date birthdate, String address, String phoneNumber) {
		State state = new State();
		StringBuilder sb = new StringBuilder("Missing fields: (");
		testField(firstName, "First name", sb, state);
		testField(lastName, "Last name", sb, state);
		testField(birthdate, "Birthdate", sb, state);
		testField(address, "Address", sb, state);
		testField(phoneNumber, "Phone number", sb, state);
		if (Status.KO.equals(state.getStatus())) {
			sb.deleteCharAt(sb.lastIndexOf(StringUtils.COMMA));
			String message = sb.append(")").toString();
			state.addMessage(message);
		}
		return state;
	}

	private void testField(Object field, String fieldName, StringBuilder sb, State state) {
		if (field == null || field.toString().equals(StringUtils.EMPTY)) {
			sb.append(fieldName).append(StringUtils.COMMA).append(StringUtils.SPACE);
			state.setStatus(Status.KO);
		}
	}

	public UserUpdater getUserUpdater(User user) {
		return new UserUpdater(user);
	}

	public State deleteUser(User user) {
		State state = new State();
		if (users.containsKey(user.getLogin())) {
			users.remove(user.getLogin());
		}
		else {
			state.setStatus(Status.KO);
			state.addMessage("User does not exist");
		}
		return state;
	}

	public User findUser(String login) {
		return users.get(login);
	}

}
