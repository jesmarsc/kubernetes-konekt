package com.kubernetes.konekt.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.LinkedHashMap;

public class RegistrationForm {
	
	@NotNull(message = "Required Field")
	@Size(min = 3, message = "Required Field")
	private String userName;
	
	@NotNull(message="Required Field")
	@Size(min=3, message="Required Field")
	private String firstName;
	
	@NotNull(message="Required Field")
	@Size(min=3, message="Required Field")
	private String lastName;
	
	@NotNull(message="Required Field")
	@Size(min=3, message="Required Field")
	private String role;
	
	@NotNull(message="Required Field")
	@Size(min=3, message="Required Field")
	@Email(message="Must be an email")
	private String email;
	
	@NotNull(message="Required Field")
	@Size(min=3, message="Required Field")
	@Email(message="Must be an email")
	private String confirmEmail;
	
	@NotNull(message="Required Field")
	@Size(min=3, message="Required Field")
	private String password;
	
	@NotNull(message="Required Field")
	@Size(min=3, message="Required Field")
	private String confirmPassword;
	
	private LinkedHashMap<String, String> accountRoles;
		
	public RegistrationForm() {
		accountRoles = new LinkedHashMap<String,String>();
		accountRoles.put("ROLE_USER", "User");
		accountRoles.put("ROLE_PROVIDER", "Provider");
	}
	
	public LinkedHashMap<String, String> getAccountRoles() {
		return accountRoles;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getConfirmEmail() {
		return confirmEmail;
	}
	
	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setAccountRoles(LinkedHashMap<String, String> accountRoles) {
		this.accountRoles = accountRoles;
	}
	
}
