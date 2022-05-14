package co.edu.icesi.dev.uccareapp.transport.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.edu.icesi.dev.uccareapp.transport.model.UserApp;
import co.edu.icesi.dev.uccareapp.transport.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
private UserRepository ur;
	
	@Autowired
	public CustomUserDetailsService(UserRepository ur) {
		this.ur = ur;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserApp userApp = ur.findByUsername(username).get();
		System.out.println(userApp.getUsername());
		if (userApp != null) {
			
			User.UserBuilder builder = User.withUsername(username).password(userApp.getPassword()).roles(userApp.getType().toString());
			return builder.build();
		} else {
			throw new UsernameNotFoundException("User not found.");
		}
	}

}
