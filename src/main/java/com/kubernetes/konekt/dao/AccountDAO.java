package com.kubernetes.konekt.dao;

import java.util.List;

import com.kubernetes.konekt.entity.Account;

public interface AccountDAO {
	
	public List<Account> getAccounts();
	
	public Account findByUserName(String userName);

	public boolean saveAccount(Account newAccount);
	
	Account getAccount(int accountId);
	public void updateAccountTables(Account uAccount);

	
}
