package com.rayanfadhlaoui.domain.services.account;

import java.time.LocalDate;
import java.util.List;

import com.rayanfadhlaoui.domain.model.entities.Account;
import com.rayanfadhlaoui.domain.model.entities.User;
import com.rayanfadhlaoui.domain.model.other.State;
import com.rayanfadhlaoui.domain.model.other.State.Status;
import com.rayanfadhlaoui.domain.services.user.UserManagement;
import com.rayanfadhlaoui.domain.services.utils.Generator;

public class AccountManagement {

	private UserManagement userManagement;

	private AccountRepository accountRepository;

	private Generator generator;

	AccountManagement(UserManagement userManagement, Generator generator, AccountRepository accountRepository) {
		this.generator = generator;
		this.userManagement = userManagement;
		this.accountRepository = accountRepository;
	}

	public void createAccount() {
		String accountNumber = generator.generateAccountNumber();
		LocalDate creationDate = LocalDate.now();
		Account account = new Account(accountNumber, creationDate);
		accountRepository.save(account);
	}

	public List<Account> getAllAccounts() {
		return accountRepository.getAll();
	}

	public State deleteAccount(String accountNumber) {
		State state = new State();
		Account account = accountRepository.findAccount(accountNumber);
		if (account != null && !account.hasUser()) {
			accountRepository.deleteAccount(account);
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
		return accountRepository.findAccount(accountNumber);
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

		if (amount < 0) {
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
