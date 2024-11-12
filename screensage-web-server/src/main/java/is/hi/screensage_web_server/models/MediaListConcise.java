package is.hi.screensage_web_server.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
/*
 * Concise representation of a user's media list
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MediaListConcise {
  private int id;
  private String title;
  private boolean watchlist;
  private List<Integer> sharedWith;

  public MediaListConcise(int id, String title, boolean watchlist, List<Integer> sharedWith) {
    this.id = id;
    this.title = title;
    this.watchlist = watchlist;
    this.sharedWith = sharedWith;
  }

  /**
   * Gets the unique identifier of the media list.
   *
   * @return the unique identifier of the media list
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the unique identifier of the media list.
   *
   * @param id the unique identifier to set
   */
  public void setId(int id) {
    this.id = id;
  }

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
