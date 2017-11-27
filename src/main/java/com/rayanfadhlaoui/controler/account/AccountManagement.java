package com.rayanfadhlaoui.controler.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rayanfadhlaoui.controler.user.Generator;
import com.rayanfadhlaoui.controler.user.UserManagement;
import com.rayanfadhlaoui.model.entities.Account;
import com.rayanfadhlaoui.model.entities.User;
import com.rayanfadhlaoui.model.other.State;
import com.rayanfadhlaoui.model.other.State.Status;

public class AccountManagement {

	private static AccountManagement INSTANCE;
	private UserManagement userManagement;

	private Map<String, Account> accounts;
	private Generator generator;

	private AccountManagement() {
		accounts = new HashMap<>();
		generator = Generator.getInstance();
		userManagement = UserManagement.getInstance();
	}

	public static AccountManagement getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AccountManagement();
		}
		return INSTANCE;
	}

	public void createAccount() {
		String accountNumber = generator.generateAccountNumber();
		Date creationDate = new Date();
		Account account = new Account(accountNumber, creationDate);
		accounts.put(accountNumber, account);
	}

	public List<Account> getAllAccounts() {
		return new ArrayList<>(accounts.values());
	}

	public State deleteAccount(String accountNumber) {
		State state = new State();
		Account account = accounts.get(accountNumber);
		if (account != null && !account.hasUser()) {
			accounts.remove(accountNumber);
		} else {
			handleDeleteError(state, account);
		}
		return state;
	}

	private void handleDeleteError(State state, Account account) {
		state.setStatus(Status.KO);
		if (account == null) {
			state.addMessage("Account does not exist");
		} else {
			state.addMessage("Impossible to delete accounts link to a user");
		}
	}

	public Account findAccount(String accountNumber) {
		return accounts.get(accountNumber);
	}

	public State wisdrawMoney(String accountNumber, int amount) {
		State state = new State();
		Account account = findAccount(accountNumber);
		if (account == null) {
			state.setStatus(Status.KO);
			state.addMessage("Account does not exist");
			return state;
		}

		Integer balance = account.getBalance();
		if (balance >= amount) {
			account.setBalance(balance - amount);
		} else {
			state.setStatus(Status.KO);
			state.addMessage("The account did not have enough money to allow the wisdraw");
		}
		return state;
	}

	public State depositMoney(String accountNumber, int amount) {
		State state = new State();
		Account account = findAccount(accountNumber);
		if (account == null) {
			state.setStatus(Status.KO);
			state.addMessage("Account does not exist");
			return state;
		}
		
		if(amount < 0) {
			state.setStatus(Status.KO);
			state.addMessage("Impossible to deposit a negative amount");
			return state;
		}

		Integer balance = account.getBalance();
		account.setBalance(balance + amount);

		return state;
	}

	public State linkAccountToUser(String accountNumber, String login) {
		State state = new State();
		User user = userManagement.findUser(login);
		Account account = findAccount(accountNumber);
		handleDataPresence(state, user, account);

		if (Status.OK.equals(state.getStatus())) {
			user.addAccount(account);
			account.setUser(user);
		}

		return state;
	}

	private void handleDataPresence(State state, User user, Account account) {
		if (user == null) {
			state.setStatus(Status.KO);
			state.addMessage("Missing user");
		}

		if (account == null) {
			state.setStatus(Status.KO);
			state.addMessage("Missing account");
		}
	}

	public State dissociateAccountFromUser(String accountNumber, String login) {
		State state = new State();
		User user = userManagement.findUser(login);
		Account account = findAccount(accountNumber);
		handleDataPresence(state, user, account);
		if (Status.OK.equals(state.getStatus())) {
			user.remove(account);
			account.setUser(null);
		}

		return state;
	}
}
