package src.ddpsc.authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import src.ddpsc.database.user.UserDao;
import src.ddpsc.database.user.DbUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Our custom authentication manager. Nothing to complex goes on, just password matching using 
 * StandardPasswordEncoder.
 *  
 * @throws {@link BadCredentialsException}
 */
public class CustomAuthenticationManager implements AuthenticationManager {

	private static Logger log = Logger.getLogger("service");
	
	
	@Autowired
	private UserDao userDao;
	private StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder();

	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		DbUser user = null;
		try {
			// Retrieve user details from database
			user = userDao.findByUsername(auth.getName());
		}
		
		catch (CannotGetJdbcConnectionException e) {
			String accessErrorMessage = "Cannot access user-data database.";
			log.error(accessErrorMessage, e);
			throw new AuthenticationServiceException(accessErrorMessage);
		}
		catch (IndexOutOfBoundsException e) {
			String existenceErrorMessage = "User " + auth.getName() + "does not exist."; 
			log.error(existenceErrorMessage, e);
			throw new UsernameNotFoundException(existenceErrorMessage);
		}
		catch (Exception e) {
			log.error("Unknown exception: " + e.getMessage(), e);
		}
		
		// Check if the user input password matches the password found in the user-data database
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		boolean passwordValid = passwordEncoder.matches((CharSequence) auth.getCredentials(), encodedPassword);
		if (passwordValid) {
			log.info("Authentication Successful for " + user.getUsername() + ", generating token.");
			return new UsernamePasswordAuthenticationToken(auth.getName(), auth.getCredentials(), getAuthorities(user));
		}
		
		else {
			String invalidErrorMessage = "Invalid password for the username " + auth.getName();
			log.error(invalidErrorMessage);
			throw new BadCredentialsException(invalidErrorMessage);
		}
	}
	
	
	/**
	 * Utility function for things that want to check password matching outside
	 * of the security contexts
	 * 
	 * @param user
	 * @param password
	 * @return
	 */
	public static boolean validateCredentials(DbUser user, String password){
		return (new StandardPasswordEncoder().matches((CharSequence) password, user.getPassword()));
	}
	
	  
	/**
	 * Retrieves the correct ROLE type depending on the access level, where access level is an Integer.
	 * Basically, this interprets the access value whether it's for a regular user or admin.
	 * 
	 * @param access an integer value representing the access of the user
	 * @return collection of granted authorities
	 */
	public Collection<GrantedAuthority> getAuthorities(DbUser user) {
		// Create a list of grants for this user
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(2);
		authList.add(new SimpleGrantedAuthority("ROLE_USER"));
		if (user.getAuthority().equals("ROLE_ADMIN")){
			log.debug("Grant ROLE_ADMIN to this user");
			authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		return authList;
	}
	
}