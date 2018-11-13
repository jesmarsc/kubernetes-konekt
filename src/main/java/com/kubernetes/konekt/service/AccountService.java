package com.kubernetes.konekt.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.kubernetes.konekt.entity.Account;
import com.kubernetes.konekt.entity.RegistrationForm;

public interface AccountService extends UserDetailsService {
	
	public List<Account> getAccounts();
	
	public Account findByUserName(String userName);
	
	public boolean saveAccount(RegistrationForm form);

	public Account getAccount(int accountId);
}
