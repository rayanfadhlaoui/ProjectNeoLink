package com.rayanfadhlaoui.domain.services.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rayanfadhlaoui.domain.model.entities.User;

public class InMemoryUserRepository implements UserRepository {

	private final Map<String, User> users;

	public InMemoryUserRepository() {
		users = new HashMap<>();
	}

	@Override
	public void saveUser(User user) {
		users.put(user.getLogin(), user);
	}

	@Override
	public User findUser(String login) {
		return users.get(login);
	}

	@Override
	public List<User> findAllUser() {
		return new ArrayList<>(users.values());
	}

	@Override
	public void deleteUser(User user) {
		users.remove(user.getLogin());
	}

}
