package is.hi.screensage_web_server.entities;

import jakarta.persistence.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import is.hi.screensage_web_server.models.MediaListConcise;

/**
 * Represents a user in the system.
 */
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class Users {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  /**
   * The username of the user.
   * This must be unique across all users.
   */
  private String username;

  /**
   * The password of the user.
   * This field is ignored during JSON serialization for security reasons.
   */
  @JsonIgnore
  private String password;

  @JsonIgnore
  private int passwordLength;

  private String profileImg;

  @JsonIgnore
  private String profileImgId;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  @OrderBy("created_at DESC")
  private List<Review> reviews;

  @Transient
  private List<MediaListConcise> lists;
  @Transient
  private List<MediaListConcise> watchlists;

  /**
   * Constructs a new user with the specified username and password.
   *
   * @param username the username of the new user
   * @param password the password of the new user
   */
  public Users(String username, String password, int passwordLength) {
    this.username = username;
    this.password = password;
    this.passwordLength = passwordLength;
  }

  /**
   * Constructs a new user with the specified username and password.
   *
   * @param username the username of the new user
   * @param password the password of the new user
   * @param profileImg the profile image of the new user
   * @param profileImgId the ID of the profile image
   */
  public Users(
    String username, 
    String password, 
    int passwordLength, 
    String profileImg, 
    String profileImgId
  ) {
    this.username = username;
    this.password = password;
    this.passwordLength = passwordLength;
    this.profileImg = profileImg;
    this.profileImgId = profileImgId;
  }

  /**
   * Default constructor for JPA.
   */
  public Users() {
  }

  /**
   * Returns the unique identifier of the user.
   *
   * @return the user's ID
   */
  public int getId() {
    return id;
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
   * Returns the password of the user.
   *
   * @return the password of the user
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the password of the user.
   *
   * @param password the password to set for the user
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Returns the password length of the user.
   *
   * @return the password length of the user
   */
  public int getPasswordLength() {
    return passwordLength;
  }

  /**
   * Sets the password length of the user.
   *
   * @param password the password length to set for the user
   */
  public void setPasswordLength(int passwordLength) {
    this.passwordLength = passwordLength;
  }

  /**
   * Returns a password placeholder for the password of the user.
   *
   * @return a password placeholder for the password of the user
   */
  public String getPasswordPlaceholder() {
    return "*".repeat(passwordLength);
  }

  /**
   * Sets the profile image of the user.
   *
   * @param profileImg the image url to set for the user
   */
  public void setProfileImg(String profileImg) {
    this.profileImg = profileImg;
  }

  /**
   * Returns the profile image of the user.
   *
   * @return the profile image of the user
   */
  public String getProfileImg() {
    return profileImg;
  }

  /**
   * Sets the ID of the user's profile image url.
   *
   * @param profileImgId the ID of the user's profile image url to be set
   */
  public void setProfileImgId(String profileImgId) {
    this.profileImgId = profileImgId;
  }

  /**
   * Returns the ID of the user's profile image url.
   *
   * @return the ID of the user's profile image url
   */
  public String getProfileImgId() {
    return profileImgId;
  }

  /**
   * Retrieves the list of media lists in concise format made by the user.
   *
   * @return a list of {@link MediaListConcise} objects representing media lists.
   */
  public List<MediaListConcise> getLists() {
    return lists;
  }

  /**
  * Sets the list of media lists made by the user.
  *
  * @param lists a list of {@link MediaListConcise} objects to set as media lists.
  */
  public void setLists(List<MediaListConcise> lists) {
    this.lists = lists;
  }

  /**
  * Retrieves the list of media watchlists in concise format made by the user.
  *
  * @return a list of {@link MediaListConcise} objects representing media watchlists.
  */
  public List<MediaListConcise> getWatchlists() {
    return watchlists;
  }

  /**
  * Sets the list of media watchlists made by the user.
  *
  * @param watchlists a list of {@link MediaListConcise} objects to set as media watchlists.
  */
  public void setWatchlists(List<MediaListConcise> watchlists) {
    this.watchlists = watchlists;
  }
}
