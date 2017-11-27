package com.rayanfadhlaoui.model.entities;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.rayanfadhlaoui.controler.utils.DateUtils;
import com.rayanfadhlaoui.controler.utils.StringUtils;

public class User {

	private final String login;
	
	private final String firstName;
	private final String lastName;
	private final Date birthdate;
	private final String address;
	private final String phoneNumber;
	private final List<Account> accounts;

	public User(String login, String firstName, String lastName, Date birthdate, String address, String phoneNumber) {
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthdate = birthdate;
		this.address = address;
		this.phoneNumber = phoneNumber;
		accounts = new LinkedList<>();
	}
	
	public String getLogin() {
		return login;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public String getAddress() {
		return address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public List<Account> getAccounts() {
		return accounts;
	}
	
	public void addAccount(Account account) {
		accounts.add(account);
	}
	

	public void remove(Account account) {
		accounts.remove(account);
	}
	
	@Override
	public String toString() {
		
		return new StringBuilder()
		.append("Login: ").append(login).append(StringUtils.LINE_BREAK)
		.append("First name : ").append(firstName).append(StringUtils.LINE_BREAK)
		.append("Last name : ").append(lastName).append(StringUtils.LINE_BREAK)
		.append("Birthdate : ").append(DateUtils.display(birthdate)).append(StringUtils.LINE_BREAK)
		.append("Adrress: ").append(address).append(StringUtils.LINE_BREAK)
		.append("Phone number : ").append(phoneNumber)
		.toString();
		
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof User) {
			User user = (User) o;
			return user.login.equals(login);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int result = 17;
        result = 31 * result + login.hashCode();
        return result;
	}


}
