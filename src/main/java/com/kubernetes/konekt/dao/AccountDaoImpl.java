package com.kubernetes.konekt.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kubernetes.konekt.entity.Account;

@Repository
public class AccountDaoImpl implements AccountDao {

	@Autowired
	private EntityManager factory;
	
	@Override
	public Account findByUserName(String userName) {
		Session currentSession = factory.unwrap(Session.class);

		Query<Account> theQuery = currentSession.createQuery("from Account where userName=:uName", Account.class);
		theQuery.setParameter("uName", userName);
		Account account = null;
		
		try {
			account = theQuery.getSingleResult();
		} catch (Exception e) {
			account = null;
		}
		return account;
	}
	
	@Override
	public List<Account> getAccounts() {
		Session currentSession = factory.unwrap(Session.class);
		
		Query<Account> theQuery = 
				currentSession.createQuery("FROM Account", Account.class);
		
		List<Account> accounts = theQuery.getResultList();
		
		return accounts;
	}

	@Override
	public boolean saveAccount(Account newAccount) {

		Session currentSession = factory.unwrap(Session.class);
		
		Query<Account> theQuery = 
				currentSession.createQuery("FROM Account where email = :email ", Account.class);
		
		theQuery.setParameter("email", newAccount.getEmail());
		
		List<Account> matchingEmails = theQuery.getResultList();
		
		if(!matchingEmails.isEmpty()) {
			return false;
		}
		
		currentSession.save(newAccount);
		return true;
	}

	@Override
	public void updateAccountTables(Account uAccount) {
		Session currentSession = factory.unwrap(Session.class);
		currentSession.saveOrUpdate(uAccount) ;
	}

}
