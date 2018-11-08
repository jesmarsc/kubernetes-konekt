package com.kubernetes.konekt.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kubernetes.konekt.entity.UserAccount;

@Repository
public class UserAccountDAOImpl implements UserAccountDAO {

	@Autowired
	private EntityManager factory;
	
	@Override
	@Transactional
	public List<UserAccount> getUserAccounts() {
		Session currentSession = factory.unwrap(Session.class);
		
		Query<UserAccount> theQuery = 
				currentSession.createQuery("FROM UserAccount", UserAccount.class);
		
		List<UserAccount> userAccounts = theQuery.getResultList();
		
		return userAccounts;
	}

}
