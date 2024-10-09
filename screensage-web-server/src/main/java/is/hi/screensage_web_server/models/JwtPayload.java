package is.hi.screensage_web_server.models;

/**
 * Represents the payload for a JSON Web Token (JWT).
 * 
 * This class holds the essential information encoded in the JWT,
 * including the user's ID, username, and the token itself.
 */
public class JwtPayload {
  // private int id;
  private Users user;
  private String token;

  /**
   * Constructs a new JwtPayload with the specified user ID, username, and token.
   *
   * @param id the unique identifier of the user
   * @param username the username of the user
   * @param token the JWT token string
   */
  public JwtPayload(Users user, String token) {
    // this.id = id;
    this.user = user;
    this.token = token;
  }

  public Users getUser() {
    return user;
  }

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
