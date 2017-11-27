package com.rayanfadhlaoui.domain.services.user;

import java.util.List;

import com.rayanfadhlaoui.domain.model.entities.User;

public interface UserRepository {
	public void saveUser(User user);
	public User findUser(String login);
	public List<User> findAllUser();
	public void deleteUser(User user);
}
