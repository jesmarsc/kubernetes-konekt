package com.kubernetes.konekt.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kubernetes.konekt.dao.AccountDao;
import com.kubernetes.konekt.dao.RoleDao;
import com.kubernetes.konekt.entity.Account;
import com.kubernetes.konekt.entity.Role;
import com.kubernetes.konekt.form.RegistrationForm;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Account account = accountDao.findByUserName(userName);
		if (account == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(account.getUserName(), account.getPassword(),
				mapRolesToAuthorities(account.getRoles()));
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}
	
	@Override
	@Transactional
	public Account findByUserName(String userName) {
		return accountDao.findByUserName(userName);
	}
	
	@Override
	@Transactional
	public List<Account> getAccounts() {
		return accountDao.getAccounts();
	}
	
	@Override
	@Transactional
	public boolean saveAccount(RegistrationForm form) {
		Account newAccount = new Account();
		newAccount.setUserName(form.getUserName());
		newAccount.setFirstName(form.getFirstName());
		newAccount.setLastName(form.getLastName());
		newAccount.setEmail(form.getEmail());
		newAccount.setPassword(passwordEncoder.encode(form.getPassword()));
		newAccount.setRoles(Arrays.asList(roleDao.findRoleByName(form.getRole())));
		
		return accountDao.saveAccount(newAccount);
	}

	@Override
	@Transactional
	public void updateAccountTables(Account uAccount) {
		accountDao.updateAccountTables(uAccount);
	}
	
}
