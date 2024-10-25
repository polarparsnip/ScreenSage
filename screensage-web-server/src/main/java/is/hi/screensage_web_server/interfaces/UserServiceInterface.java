package is.hi.screensage_web_server.interfaces;

import org.springframework.web.multipart.MultipartFile;

import is.hi.screensage_web_server.entities.Users;
import is.hi.screensage_web_server.models.JwtPayload;
import is.hi.screensage_web_server.models.UserProfile;

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
   * @return The newly created user object
   */
  Users register(String username, String password);

  /**
   * Registers a new user with the provided username, password and profile image file.
   *
   * This method performs necessary checks on the provided username and password.
   *
   * @param username the username of the new user
   * @param password the password of the new user
   * @param imageFile the file corresponding to the profile picture of the new user
   * @return The newly created user object
   */
  Users register(String username, String password, MultipartFile imageFile);

  /**
   * Authenticates a user using the provided username and password.
   *
   * This method checks the provided credentials and returns an appropriate response.
   *
   * @param username the username of the user attempting to log in
   * @param password the password of the user attempting to log in
   * @return JwtPayload containing the authorized user and a JWT token
   */
  JwtPayload login(String username, String password);

  /**
   * Retrieves the profile of the currently authenticated user.
   *
   * @return the {@link UserProfile} of the authenticated user
   */
  public UserProfile getUserProfile();

  /**
   * Updates the username for a specific user.
   *
   * @param userId the ID of the user whose username is to be updated
   * @param newUsername the new username to set for the user
   * @return a {@link JwtPayload} containing updated user information and authentication data
   */
  public JwtPayload updateUsername(int userId, String newUsername);

  /**
   * Updates the password for a specific user.
   *
   * @param userId the ID of the user whose password is to be updated
   * @param newPassword the new password to set for the user
   * @return the updated {@link Users} object after the password change
   */
  public Users updatePassword(int userId, String newPassword);

  /**
   * Checks if a user exists with the specified username.
   *
   * @param username the username to search for
   * @return {@code true} if a user exists with the given username, otherwise {@code false}
   */
  public boolean userExists(String username);

  /**
   * Checks if a user exists with the specified user ID.
   *
   * @param userId the ID of the user to search for
   * @return {@code true} if a user exists with the given user ID, otherwise {@code false}
   */
  public boolean userExists(int userId);

  /**
   * Finds and retrieves a user by their username.
   *
   * @param username the username of the user to find
   * @return the {@link Users} object for the specified username, or {@code null} if not found
   */
  public Users findUser(String username);

  /**
   * Finds and retrieves a user by their user ID.
   *
   * @param userId the ID of the user to find
   * @return the {@link Users} object for the specified user ID, or {@code null} if not found
   */
  public Users findUser(int userId);

  /**
   * Retrieves a reference to a user by their user ID without fully loading the entity.
   *
   * @param userId the ID of the user to retrieve a reference for
   * @return a reference to the {@link Users} entity with the specified ID
   */
  public Users getUserReferenceById(int userId);

}