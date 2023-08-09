package com.example.test.Config;

import com.example.test.Entity.Customer;
import com.example.test.Entity.Role;
import com.example.test.Entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MyUserDetails implements UserDetails {

//	private Customer customer;
	private Users user;
	public MyUserDetails(Users user) {
		this.user=user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<Role> roles = user.getRoleList();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		for(Role role:roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.user.getStatus() == 1;
//		return Boolean.parseBoolean(user.getStatus());
	}

	public Users getUser() {
		return  this.user;
	}

	public boolean hasRole(String roleName){
		return user.hasRole(roleName);
	}
}
