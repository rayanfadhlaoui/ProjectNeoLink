package com.rayanfadhlaoui.model.entities;

import java.util.Date;

import com.rayanfadhlaoui.controler.utils.DateUtils;
import com.rayanfadhlaoui.controler.utils.StringUtils;

public class Account {
	private String accountNumber;
	private Date creationDate;
	private Integer balance;
	private User user;

	public Account(String accountNumber, Date creationDate) {
		this.accountNumber = accountNumber;
		this.creationDate = creationDate;
		balance = 0;
		user = null;
	}

	public Integer getBalance() {
		return balance;
	}

	public Date getcreationDate() {
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
				.append("Creation date : ").append(DateUtils.display(creationDate)).append(StringUtils.LINE_BREAK) 
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
