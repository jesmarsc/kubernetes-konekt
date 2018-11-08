package com.kubernetes.konekt.dao;

import java.util.List;

import com.kubernetes.konekt.entity.UserAccount;

public interface UserAccountDAO {
	public List<UserAccount> getUserAccounts();

	public boolean saveUser(UserAccount newUser);
}
