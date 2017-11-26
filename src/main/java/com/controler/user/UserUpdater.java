package com.controler.user;

import java.util.Date;

import com.model.entities.User;

public class UserUpdater {
	private User user;
	private String firstName;
	private String lastName;
	private Date birthdate;
	private String address;
	private String phoneNumber;
	
	public UserUpdater(User user) {
		this.user = user;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		
	}

	public User getUser() {
		String loggin = user.getLogin();
		String newFirstName = firstName != null ? firstName : user.getFirstName();
		String newLastName = lastName != null ? lastName : user.getLastName();
		Date newBirthdate = birthdate != null ? birthdate : user.getBirthdate();
		String newAddress = address != null ? address : user.getAddress();
		String newPhoneNumber = phoneNumber != null ? phoneNumber : user.getPhoneNumber();
		
		return new User(loggin, newFirstName, newLastName, newBirthdate, newAddress, newPhoneNumber);
	}
}
