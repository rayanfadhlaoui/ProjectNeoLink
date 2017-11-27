package com.rayanfadhlaoui.model.other;

import java.util.ArrayList;
import java.util.List;

public class State {

	private Status status;
	private List<String> messages;

	public State() {
		status = Status.OK;
		messages = new ArrayList<>();
	}

	public Status getStatus() {
		return status;
	}

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
