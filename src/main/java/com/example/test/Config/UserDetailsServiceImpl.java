package com.example.test.Config;

import com.example.test.Entity.Users;
import com.example.test.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userRepository.getUsersByUsername(username);

		if(user == null) {
			throw new UsernameNotFoundException("Could not find User with username: " + username);
		}
		return new MyUserDetails(user);
	}
	private static Collection<? extends GrantedAuthority> getAuthorities(Users user) {
		String[] userRoles = user.getRoleList().stream().map((role) -> role.getName()).toArray(String[]::new);
		Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
		return authorities;
	}
}
