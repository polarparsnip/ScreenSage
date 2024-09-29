package is.hi.screensage_web_server.models;

/**
 * Represents the payload for a JSON Web Token (JWT).
 * 
 * This class holds the essential information encoded in the JWT,
 * including the user's ID, username, and the token itself.
 */
public class JwtPayload {
  private int id;
  private String username;
  private String token;

  /**
   * Constructs a new JwtPayload with the specified user ID, username, and token.
   *
   * @param id the unique identifier of the user
   * @param username the username of the user
   * @param token the JWT token string
   */
  public JwtPayload(int id, String username, String token) {
    this.id = id;
    this.username = username;
    this.token = token;
  }

  /**
   * Returns the ID of the user.
   *
   * @return the user's ID
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the ID of the user.
   *
   * @param id the ID to set for the user
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Returns the username of the user.
   *
   * @return the username of the user
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the username of the user.
   *
   * @param username the username to set for the user
   */
  public void setUsername(String username) {
    this.username = username;
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
