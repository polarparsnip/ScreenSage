package is.hi.screensage_web_server.models;

/**
 * Request body for a user submitted review for a piece of media.
 */
public class ReviewRequest {
  private double rating;
  private String content;

  /**
   * Gets the rating.
   *
   * @return the rating given by the user for the media.
   */
  public double getRating() {
    return rating;
  }

  /**
   * Sets the rating.
   *
   * @param rating the rating given by the user for the media.
   */
  public void setRating(double rating) {
    this.rating = rating;
  }

  /**
   * Gets the content of the review.
   *
   * @return the content of the review provided by the user.
   */
  public String getContent() {
    return content;
  }

  /**
   * Sets the content of the review.
   *
   * @param content the content of the review provided by the user.
   */
  public void setContent(String content) {
    this.content = content;
  }

}
