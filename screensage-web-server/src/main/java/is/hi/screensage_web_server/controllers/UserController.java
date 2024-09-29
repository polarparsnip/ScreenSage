package is.hi.screensage_web_server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import is.hi.screensage_web_server.models.UserRequest;
import is.hi.screensage_web_server.services.UserService;

/**
 * UserController is a REST controller that handles user-related operations.
 *
 * The controller relies on the UserService to manage the business 
 * logic for all operations.
 */
@RestController
public class UserController {

  @Autowired
  private UserService userService;

  /**
   * Registers a new user with the provided username and password.
   *
   * @param userRequest the request body containing the username and password for registration
   * @return a ResponseEntity containing the result of the registration process,
   *         including success or error information
   */
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody UserRequest userRequest) {
    String username = userRequest.getUsername();
    String password = userRequest.getPassword();
    return userService.register(username, password);
  }

  /**
   * Authenticates a user with the provided username and password.
   *
   * @param userRequest the request body containing the username and password for login
   * @return a ResponseEntity containing the result of the login process,
   *         including a JWT token on successful authentication or an error message
   */
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody UserRequest userRequest) {
    String username = userRequest.getUsername();
    String password = userRequest.getPassword();
    return userService.login(username, password); 
  }

}
