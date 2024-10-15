package is.hi.screensage_web_server.models;

import is.hi.screensage_web_server.entities.Users;

/**
 * Represents the payload for a JSON Web Token (JWT).
 * 
 * This class holds the essential information encoded in the JWT,
 * including the user's information and the token itself.
 */
public class JwtPayload {
  private Users user;
  private String token;

  /**
   * Constructs a new JwtPayload with the specified user information and token.
   *
   * @param user the user information
   * @param token the JWT token string
   */
  public JwtPayload(Users user, String token) {
    this.user = user;
    this.token = token;
  }

  /**
   * Returns the user.
   *
   * @return the user
   */
  public Users getUser() {
    return user;
  }

  /**
   * Sets the user.
   *
   * @param user the user to set
   */
  public void setUser(Users user) {
    this.user = user;
  }

  /**
   * Returns the JWT token string.
   *
   * @return the JWT token
   */
  public String getToken() {
    return token;
  }

  /**
   * Sets the JWT token string.
   *
   * @param token the JWT token to set
   */
  public void setToken(String token) {
    this.token = token;
  }
}
