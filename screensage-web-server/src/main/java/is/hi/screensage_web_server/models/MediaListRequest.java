package is.hi.screensage_web_server.models;

import java.util.List;

/**
 * Represents a post request payload for a media list.
 */
public class MediaListRequest {
  private String type;
  private List<MediaListItemRequest> mediaListItems;
  private boolean watchlist;
  private List<Integer> sharedWith;

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
    if (!type.equals("tv") && !type.equals("movie")) {
      return;
    }
    this.type = type;
  }

  /**
   * Gets the media items in the list.
   *
   * @return the list of media items
   */
  public List<MediaListItemRequest> getMediaListItems() {
    return mediaListItems;
  }

  /**
   * Sets media items in the list.
   *
   * @param mediaListItems the list of media items to set
   */
  public void setMediaListItems(List<MediaListItemRequest> mediaListItems) {
    this.mediaListItems = mediaListItems;
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
