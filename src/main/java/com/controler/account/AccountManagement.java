package com.controler.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.controler.user.Generator;
import com.model.entities.Account;
import com.model.other.State;
import com.model.other.State.Status;

public class AccountManagement {

	private static AccountManagement INSTANCE;

	private Map<String, Account> accounts;
	private Generator generator;

	private AccountManagement() {
		accounts = new HashMap<>();
		generator = Generator.getInstance();
	}

	public static AccountManagement getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AccountManagement();
		}
		return INSTANCE;
	}

	public void reset() {
		accounts = new HashMap<>();
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
		if (accounts.containsKey(accountNumber)) {
			accounts.remove(accountNumber);
		} else {
			state.setStatus(Status.KO);
			state.addMessage("Account does not exist");
		}
		return state;
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

		Integer balance = account.getBalance();
		account.setBalance(balance + amount);

		return state;
	}
}
