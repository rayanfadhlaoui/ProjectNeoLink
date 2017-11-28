package com.rayanfadhlaoui.domain.model.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.rayanfadhlaoui.domain.services.utils.StringUtils;

public class Account {
	private final DateTimeFormatter MY_PATTERN = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private final String accountNumber;
	private final LocalDate creationDate;
	private Integer balance;
	private User user;

	public Account(String accountNumber,LocalDate creationDate) {
		this.accountNumber = accountNumber;
		this.creationDate = creationDate;
		balance = 0;
		user = null;
	}

	public Integer getBalance() {
		return balance;
	}

	public LocalDate getcreationDate() {
		return creationDate;
	}

	public String getAccountNumber() {
		return accountNumber;
	}
	
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public boolean hasUser() {
		return user != null;
	}

	@Override
	public String toString() {
		return new StringBuilder().append("Account number: ").append(accountNumber).append(StringUtils.LINE_BREAK) 
				.append("Creation date : ").append(creationDate.format(MY_PATTERN)).append(StringUtils.LINE_BREAK) 
				.append("Balance : ").append(balance).toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Account) {
			Account account = (Account) o;
			return account.accountNumber.equals(accountNumber);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int result = 17;
        result = 31 * result + accountNumber.hashCode();
        return result;
	}
	

}
