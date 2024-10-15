package is.hi.screensage_web_server.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import is.hi.screensage_web_server.entities.Users;
import is.hi.screensage_web_server.models.UserPrincipal;
import is.hi.screensage_web_server.repositories.UserRepository;


/**
 * Service for loading user-specific data from the database.
 */
@Service
public class ScreenSageUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  /**
   * Loads a user by their username from the database.
   * 
   * This method fetches a user from the database using the {@link UserRepository}. 
   * If the user is found, it returns a {@link UserPrincipal} object representing the authenticated user.
   * If the user is not found, a {@link UsernameNotFoundException} is thrown.
   *
   * @param username the username of the user to load
   * @return a {@link UserDetails} object representing the authenticated user
   * @throws UsernameNotFoundException if no user is found with the provided username
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      Users user = userRepository.findByUsername(username);

      if (user == null) {
        System.out.println("User Not Found");
        throw new UsernameNotFoundException("User not found");
      }
          
      return new UserPrincipal(user);
    } catch (Exception e) {
      System.out.println("Error while fetching user: " + e.getMessage());
      throw new UsernameNotFoundException("Error while fetching user: ", e);
  }
  }
}
