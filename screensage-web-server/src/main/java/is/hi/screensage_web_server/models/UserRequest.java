package is.hi.screensage_web_server.models;

/**
 * Represents a request body received from the client for user login and registration.
 * 
 * This class contains the necessary information for a user to log in
 * or register, including the username and password.
 */
public class UserRequest {
  private String username;
  private String password;

  /**
   * Returns the username of the request.
   *
   * @return the username of the request
   */
  public String getUsername() {
    return username;
  }

  /**
   * Returns the password of the request.
   *
   * @return the password of the request
   */
  public String getPassword() {
    return password;
  }
}