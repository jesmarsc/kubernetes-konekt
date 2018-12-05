package com.kubernetes.konekt.dao;

import java.util.List;

import com.kubernetes.konekt.entity.Account;

public interface AccountDao {
	
	public Account findByUserName(String userName);
	
	public List<Account> getAccounts();

	public boolean saveAccount(Account newAccount);
	
	public void updateAccountTables(Account uAccount);
	
}
