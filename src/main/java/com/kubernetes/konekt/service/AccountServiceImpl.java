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

import com.kubernetes.konekt.dao.AccountDAO;
import com.kubernetes.konekt.dao.RoleDao;
import com.kubernetes.konekt.entity.Account;
import com.kubernetes.konekt.entity.RegistrationForm;
import com.kubernetes.konekt.entity.Role;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private AccountDAO accountDAO;
	
	@Autowired
	private RoleDao roleDAO;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	@Transactional
	public List<Account> getAccounts() {
		return accountDAO.getAccounts();
	}
	
	@Override
	@Transactional
	public Account findByUserName(String userName) {
		return accountDAO.findByUserName(userName);
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
		newAccount.setRoles(Arrays.asList(roleDAO.findRoleByName(form.getRole())));
		
		return accountDAO.saveAccount(newAccount);
	}
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Account account = accountDAO.findByUserName(userName);
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
	public Account getAccount(int accountId) {
		Account account = accountDAO.getAccount(accountId);
		
		return account;
	}
	
}
