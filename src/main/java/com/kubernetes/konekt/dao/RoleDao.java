package com.kubernetes.konekt.dao;

import com.kubernetes.konekt.entity.Role;

public interface RoleDao {

	public Role findRoleByName(String theRoleName);
	
}
