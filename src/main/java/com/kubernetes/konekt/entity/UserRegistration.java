package com.kubernetes.konekt.entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.LinkedHashMap;

public class UserRegistration {
	
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
		
	public UserRegistration() {
		accountRoles = new LinkedHashMap<String,String>();
		accountRoles.put("User", "User");
		accountRoles.put("Provider", "Provider");
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
}
