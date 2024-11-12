package is.hi.screensage_web_server.models;

/**
 * Represents an item in a media list payload.
 */
public class MediaListItemRequest {
  private int mediaId;
  private String mediaTitle;
  private String mediaSummary;
  private String mediaImg;
  private String type;

  /**
   * Gets the media ID.
   *
   * @return the media ID
   */
  public int getMediaId() {
    return mediaId;
  }

  /**
   * Sets the media ID.
   *
   * @param mediaId the media ID to set
   */
  public void setMediaId(int mediaId) {
    this.mediaId = mediaId;
  }

  /**
   * Gets the media title.
   *
   * @return the media title
   */
  public String getMediaTitle() {
    return mediaTitle;
  }

  /**
   * Sets the media title.
   *
   * @param mediaTitle the media title to set
   */
  public void setMediaTitle(String mediaTitle) {
    this.mediaTitle = mediaTitle;
  }

    /**
   * Gets the media summary.
   *
   * @return the media summary
   */
  public String getMediaSummary() {
    return mediaSummary;
  }

  /**
   * Sets media summary.
   *
   * @param mediaSummary the media summary to set
   */
  public void setMediaSummary(String mediaSummary) {
    this.mediaSummary = mediaSummary;
  }

  /**
   * Gets the media image URL.
   *
   * @return the media image URL
   */
  public String getMediaImg() {
    return mediaImg;
  }

  /**
   * Sets the media image URL.
   *
   * @param mediaImg the media image URL to set
   */
  public void setMediaImg(String mediaImg) {
    this.mediaImg = mediaImg;
  }
  
    /**
   * Returns the media type of the media.
   * Either shows, anime or movies.
   *
   * @return the media type
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the media type of the media.
   * Either shows, anime or movies.
   * 
   * @param type the media type to be set
   */
  public void setType(String type) {
    if (
      !type.equals("anime") &&
      !type.equals("shows") &&
      !type.equals("movies")
    ) {
      return;
    }
    this.type = type;
  }

}
