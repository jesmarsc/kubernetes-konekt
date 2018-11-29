package com.kubernetes.konekt.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.kubernetes.konekt.entity.Account;
import com.kubernetes.konekt.form.RegistrationForm;

public interface AccountService extends UserDetailsService {
	
	public Account findByUserName(String userName);
	
	public List<Account> getAccounts();
	
	public boolean saveAccount(RegistrationForm form);
	
	public void updateAccountTables(Account uAccount);

}
