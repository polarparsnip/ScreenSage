package is.hi.screensage_web_server.entities;

import jakarta.persistence.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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


  private String profileImg;

  @JsonIgnore
  private String profileImgId;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  @OrderBy("created_at DESC")
  List<Review> reviews;

  /**
   * Constructs a new user with the specified username and password.
   *
   * @param username the username of the new user
   * @param password the password of the new user
   */
  public Users(String username, String password) {
    this.username = username;
    this.password = password;
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
}