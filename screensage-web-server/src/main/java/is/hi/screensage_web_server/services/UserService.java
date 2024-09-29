package is.hi.screensage_web_server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import is.hi.screensage_web_server.interfaces.UserServiceInterface;
import is.hi.screensage_web_server.models.JwtPayload;
import is.hi.screensage_web_server.models.UserPrincipal;
import is.hi.screensage_web_server.models.Users;
import is.hi.screensage_web_server.repositories.UserRepository;

/**
 * Service for user-related services, including user registration and authentication.
 */
@Service
public class UserService implements UserServiceInterface {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  AuthenticationManager authManager;

  @Autowired
  private JWTService jwtService;

  private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

  final int MIN_USERNAME_LENGTH = 3;
  final int MAX_USERNAME_LENGTH = 20;
  final int MIN_PASSWORD_LENGTH = 2;
  final int MAX_PASSWORD_LENGTH = 30;

  /**
   * Registers a new user with the provided username and password.
   *
   * Performs validation checks on the username and password, ensuring they are not empty, 
   * do not contain whitespace characters, and meet specified length requirements. 
   * If the username already exists in the repository, it returns a bad request response.
   * 
   * @param username the username of the new user
   * @param password the password of the new user
   * @return ResponseEntity<?> containing the newly created user object, or an error message
   */
  @Override
  public ResponseEntity<?> register(String username, String password) {

    if (username == null || username.trim().isEmpty() || username.matches(".*\\s.*")) {
      return ResponseEntity.badRequest().body("Username cannot be empty and must not contain whitespace characters.");
    }
    if (password == null || password.trim().isEmpty() || password.matches(".*\\s.*")) {
      return ResponseEntity.badRequest().body("Password cannot be empty and must not contain whitespace characters.");
    }

    if (username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH) {
      return ResponseEntity.badRequest().body("Username must be between " + MIN_USERNAME_LENGTH + " and " + MAX_USERNAME_LENGTH + " characters.");
    }
    if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
      return ResponseEntity.badRequest().body("Password must be between " + MIN_PASSWORD_LENGTH + " and " + MAX_PASSWORD_LENGTH + " characters.");
    }

    if (userRepository.findByUsername(username) != null) {
      return ResponseEntity.badRequest().body("Username already exists.");
    }

    Users newUser = new Users(username, "");
    newUser.setPassword(encoder.encode(password));

    try {
      userRepository.save(newUser);
    } catch (Exception e) {
      System.out.println("Error occured while saving user: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while saving user.");
    }

    return ResponseEntity.ok(newUser);
  }

  /**
   * Authenticates a user using the provided username and password.
   *
   * If the authentication is successful, it generates a JWT token for the user and returns it in the response.
   * Performs validation on the username and password, ensuring they are not empty and do not contain whitespace characters.
   * 
   * @param username the username of the user attempting to log in
   * @param password the password of the user attempting to log in
   * @return ResponseEntity<?> containing the JWT token, or an error message in case of failed authentication
   */
  @Override
  public ResponseEntity<?> login(String username, String password) { 
    if (username == null || username.trim().isEmpty() || username.matches(".*\\s.*")) {
      return ResponseEntity.badRequest().body("Username cannot be empty and must not contain whitespace characters.");
    }
    if (password == null || password.trim().isEmpty() || password.matches(".*\\s.*")) {
      return ResponseEntity.badRequest().body("Password cannot be empty and must not contain whitespace characters.");
    }
    
    try {
      Authentication authentication = authManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, password)
      );

      String token = jwtService.generateToken(username);
      UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
      JwtPayload jwtPayload = new JwtPayload(authenticatedUser.getId(), authenticatedUser.getUsername(), token);

      return ResponseEntity.ok(jwtPayload);

    } catch (BadCredentialsException e) {
        return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
        System.out.println("Error during login: " + e.getMessage());
        return new ResponseEntity<>("An error occurred during login", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
