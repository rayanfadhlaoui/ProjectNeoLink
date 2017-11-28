package com.rayanfadhlaoui.domain.services.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rayanfadhlaoui.domain.model.entities.Account;

public class InMemoryAccountRepository implements AccountRepository {
	private Map<String, Account> accounts;

	public InMemoryAccountRepository() {
		accounts = new HashMap<>();
	}

	@Override
	public void save(Account account) {
		accounts.put(account.getAccountNumber(), account);
	}

	@Override
	public Account findAccount(String accountNumber) {
		return accounts.get(accountNumber);
	}

	@Override
	public void deleteAccount(Account account) {
		accounts.remove(account.getAccountNumber());
	}

	@Override
	public List<Account> getAll() {
		return new ArrayList<>(accounts.values());
	}
}
