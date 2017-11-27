package com.rayanfadhlaoui.domain.services.user;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.rayanfadhlaoui.domain.model.entities.Account;
import com.rayanfadhlaoui.domain.model.entities.User;
import com.rayanfadhlaoui.domain.model.other.State;
import com.rayanfadhlaoui.domain.model.other.State.Status;
import com.rayanfadhlaoui.domain.services.utils.Generator;
import com.rayanfadhlaoui.domain.services.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;;

public class UserManagement {

	private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("(0|\\\\+33|0033)[1-9][0-9]{8}");

	private UserRepository userRepository;

	private Generator generator;

	public UserManagement(UserRepository userRepository, Generator generator) {
		this.userRepository = userRepository;
		this.generator = generator;
	}

	public State createUser(String firstName, String lastName, LocalDate birthdate, String address, String phoneNumber) {
		State state = dataCheck(firstName, lastName, birthdate, address, phoneNumber);
		if (Status.OK.equals(state.getStatus())) {
			User user = new User(generator.generateLogin(), firstName, lastName, birthdate, address, phoneNumber);
			userRepository.saveUser(user);
		}

		return state;
	}

	private State dataCheck(String firstName, String lastName, LocalDate birthdate, String address, String phoneNumber) {
		State state = checkFieldsPresence(firstName, lastName, birthdate, address, phoneNumber);
		state = checkDataValidity(phoneNumber, state);
		return state;
	}

	public List<User> getAllUsers() {
		return userRepository.findAllUser();
	}

	public State updateUser(UserUpdater userUpdater) {
		User user = userUpdater.getUser();
		State state = dataCheck(user.getFirstName(), user.getLastName(), user.getBirthdate(), user.getAddress(), user.getPhoneNumber());
		if (Status.OK.equals(state.getStatus())) {
			userRepository.saveUser(user);
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

	private State checkFieldsPresence(String firstName, String lastName, LocalDate birthdate, String address, String phoneNumber) {
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
		if (userRepository.findUser(user.getLogin()) != null) {
			userRepository.deleteUser(user);
		} else {
			state.setStatus(Status.KO);
			state.addMessage("User does not exist");
		}
		return state;
	}

	public User findUser(String login) {
		return userRepository.findUser(login);
	}

	public List<Account> getAllAccountsAssociatedToUser(String login) {
		User user = userRepository.findUser(login);
		if (user != null) {
			return user.getAccounts();
		}
		return null;
	}

	public int getUserWealth(String login) {
		List<Account> allAccounts = getAllAccountsAssociatedToUser(login);
		AtomicInteger wealth = new AtomicInteger();
		allAccounts.forEach((account) -> wealth.addAndGet(account.getBalance()));
		return wealth.get();
	}

}
