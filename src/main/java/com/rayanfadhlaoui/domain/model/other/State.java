package com.rayanfadhlaoui.domain.model.other;

import java.util.ArrayList;
import java.util.List;

public class State {

	private Status status;
	private final List<String> messages;

	public State() {
		status = Status.OK;
		messages = new ArrayList<>();
	}

	public Status getStatus() {
		return status;
	}

	/** Retrieve all the messages.
	 * @return List of all the error messages.<br/> 
	 * If the status is 'OK', the list will be empty. */
	public List<String> getMessages() {
		return messages;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}

	public enum Status {
		OK, KO;
	}

	public void addMessage(String message) {
		messages.add(message);
	}
	
}
