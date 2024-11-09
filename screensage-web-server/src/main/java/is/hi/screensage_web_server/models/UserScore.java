package is.hi.screensage_web_server.models;

import is.hi.screensage_web_server.entities.Users;

/**
 * Data Transfer Object (DTO) for holding a user's information along with their total points.
 */
public class UserScore {
  private Users user;
  private Long totalPoints;

  /**
   * Constructs a new UserScoreDTO with the specified user and total points.
   *
   * @param user        The user object.
   * @param totalPoints The total points associated with the user.
   */
  public UserScore(Users user, Long totalPoints) {
    this.user = user;
    this.totalPoints = totalPoints;
  }

  /**
   * Gets the user associated with this DTO.
   *
   * @return The user object.
   */
  public Users getUser() {
    return user;
  }

  /**
   * Sets the user associated with this DTO.
   *
   * @param user The user object to set.
   */
  public void setUser(Users user) {
    this.user = user;
  }

  /**
   * Gets the total points associated with the user.
   *
   * @return The total points.
   */
  public Long getTotalPoints() {
    return totalPoints;
  }

  /**
   * Sets the total points associated with the user.
   *
   * @param totalPoints The total points to set.
   */
  public void setTotalPoints(Long totalPoints) {
    this.totalPoints = totalPoints;
  }

}