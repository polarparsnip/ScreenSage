package is.hi.screensage_web_server.models;

import java.util.List;

import is.hi.screensage_web_server.entities.Review;
import is.hi.screensage_web_server.entities.Users;

  /**
   * UserProfile payload.
   *
   * Contains information such as user information and recent reviews
   */
public class UserProfile {
  private Users user;
  private List<Review> recentReviews;

  /**
   * Constructs a new UserProfile object with the specified user.
   *
   * @param user the user
   */
  public UserProfile(Users user) {
    this.user = user;
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
   * Returns the most recent reviews left by the user.
   *
   * @return the list of recent reviews
   */
  public List<Review> getRecentReviews() {
    return recentReviews;
  }

  /**
   * Sets the list of recent reviews.
   *
   * @param recentReviews the list of reviews to set
   */
  public void setRecentReviews(List<Review> recentReviews) {
    this.recentReviews = recentReviews;
  }
}
