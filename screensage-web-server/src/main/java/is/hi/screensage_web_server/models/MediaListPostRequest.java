package is.hi.screensage_web_server.models;

import java.util.List;

/**
 * Represents a post request payload for a media list.
 */
public class MediaListPostRequest {
  private String title;
  private String description;
  private String type;
  private boolean watchlist;
  private List<Integer> sharedWith;

    /**
   * Returns the title of the media list.
   *
   * @return the title of the media list
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of the media list.
   * 
   * @param title the title to be set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Returns the description of the media list.
   *
   * @return the description of the media list
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of the media list.
   * 
   * @param description the description to be set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Gets the type of media the list is for (e.g., "movie" or "tv").
   *
   * @return the type of the media list
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the type of media the list is for (e.g., "movie" or "tv").
   *
   * @param type the type of media list to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Checks if the media list is a watchlist.
   *
   * @return {@code true} if it is a watchlist, {@code false} otherwise
   */
  public boolean isWatchlist() {
    return watchlist;
  }

  /**
   * Sets whether the list is a watchlist.
   *
   * @param watchlist the watchlist status to set
   */
  public void setWatchlist(boolean watchlist) {
    this.watchlist = watchlist;
  }

  /**
   * Gets the list of user IDs that this media list is shared with.
   * 
   * @return the list of shared user IDs
   */
  public List<Integer> getSharedWith() {
    return sharedWith;
  }

  /**
   * Sets the list of user IDs that this media list is shared with.
   * 
   * @param sharedWith the list of shared user IDs to set
   */
  public void serSharedWith(List<Integer> sharedWith) {
    this.sharedWith = sharedWith;
  }
}
