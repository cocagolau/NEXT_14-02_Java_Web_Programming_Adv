package me.dec7.dao.users;

import me.dec7.domain.users.User;

public interface UserDao {

	User findById(String userId);

	void create(User user);

	void update(User user);

}