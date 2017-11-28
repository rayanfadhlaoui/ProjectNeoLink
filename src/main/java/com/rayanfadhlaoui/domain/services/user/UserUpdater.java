package com.rayanfadhlaoui.domain.services.user;

import java.time.LocalDate;

import com.rayanfadhlaoui.domain.model.entities.User;

/**The UserUpdater can update an user.*/
class UserUpdater {
	private final User user;
	private String firstName;
	private String lastName;
	private LocalDate birthdate;
	private String address;
	private String phoneNumber;
	
	UserUpdater(User user) {
		this.user = user;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setBirthdate(LocalDate birthdate) {
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
		LocalDate newBirthdate = birthdate != null ? birthdate : user.getBirthdate();
		String newAddress = address != null ? address : user.getAddress();
		String newPhoneNumber = phoneNumber != null ? phoneNumber : user.getPhoneNumber();
		
		return new User(loggin, newFirstName, newLastName, newBirthdate, newAddress, newPhoneNumber);
	}
}
