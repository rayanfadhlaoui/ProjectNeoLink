package com.rayanfadhlaoui.domain.services.account;

import java.util.List;

import com.rayanfadhlaoui.domain.model.entities.Account;

public interface AccountRepository {
	public void save(Account account);
	public List<Account> getAll();
	public Account findAccount(String accountNumber);
	public void deleteAccount(Account account);
}
