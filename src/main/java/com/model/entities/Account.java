package com.model.entities;

import java.util.Date;

import com.controler.utils.DateUtils;
import com.controler.utils.StringUtils;

public class Account {
	private String accountNumber;
	private Date creationDate;
	private Integer balance;

	public Account(String accountNumber, Date creationDate) {
		this.accountNumber = accountNumber;
		this.creationDate = creationDate;
		balance = 0;
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

	@Override
	public String toString() {
		return new StringBuilder().append("Account number: ").append(accountNumber).append(StringUtils.LINE_BREAK) 
				.append("Creation date : ").append(DateUtils.display(creationDate)).append(StringUtils.LINE_BREAK) 
				.append("Balance : ").append(balance).toString();
	}

	

}
