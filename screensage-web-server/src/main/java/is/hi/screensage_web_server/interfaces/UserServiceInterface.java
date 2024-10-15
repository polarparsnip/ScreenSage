package is.hi.screensage_web_server.interfaces;

import org.springframework.http.ResponseEntity;

import io.micrometer.core.instrument.config.validate.ValidationException;
import is.hi.screensage_web_server.entities.Users;

/**
 * UserServiceInterface defines the contract for user-related services,
 * including user registration and authentication.
 *
 * This interface outlines the methods necessary for managing user 
 * accounts, allowing for user registration and authentication.
 */
public interface UserServiceInterface {

  /**
   * Registers a new user with the provided username and password.
   *
   * This method performs necessary checks on the provided username and password.
   *
   * @param username the username of the new user
   * @param password the password of the new user
   * @return ResponseEntity<?> containing the newly created user object, or an error message if registration fails
   * @throws UserAlreadyExistsException if the username is already taken
   * @throws ValidationException if the username or password do not meet the required criteria
   */
  ResponseEntity<?> register(String username, String password);

  /**
   * Authenticates a user using the provided username and password.
   *
   * This method checks the provided credentials and returns an appropriate response.
   *
   * @param username the username of the user attempting to log in
   * @param password the password of the user attempting to log in
   * @return ResponseEntity<?> containing the authentication result, which may include a JWT token or an error message
   * @throws AuthenticationException if the authentication fails due to invalid credentials
   */
  ResponseEntity<?> login(String username, String password);

  public boolean userExists(String username);

  public boolean userExists(int userId);

  public Users findUser(String username);

  public Users findUser(int userId);

  public Users getUserReferenceById(int userId);

}