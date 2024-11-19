package is.hi.screensage_web_server.entities;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "media_lists")
public class MediaList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  private Users user;
  
  private String title;
  private String description;

  @OneToMany(mappedBy = "mediaList", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("created_at DESC")
  private List<MediaListItem> mediaListItems;

  private boolean watchlist = false;
  private List<Integer> sharedWith;
  private long likeCount;
  @Transient
  private boolean userHasLiked;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Date createdAt;

  /**
   * Constructs a new MediaList with the specified user.
   *
   * @param user           the user associated with the media list
   */
  public MediaList(Users user)  {
    this.user = user;
  }

  /**
   * Constructs a new MediaList with the specified user.
   *
   * @param user        the user associated with the media list
   * @param title       the title of the media list
   * @param description the description of the media list
   */
  public MediaList(Users user, String title, String description)  {
    this.user = user;
    this.title = title;
    this.description = description;
  }

  /**
   * Constructs a new MediaList with the specified user and media IDs.
   *
   * @param user           the user associated with the media list
   * @param mediaListItems a list of {@link MediaListItem} objects to set as the media items of the list.
   */
  public MediaList(Users user, List<MediaListItem> mediaListItems)  {
    this.user = user;
    this.mediaListItems = mediaListItems;
  }

    /**
   * Constructs a new MediaList with the specified user and media IDs.
   *
   * @param user           the user associated with the media list
   * @param title          the title of the media list
   * @param description    the description of the media list
   * @param mediaListItems a list of {@link MediaListItem} objects to set as the media items of the list.
   */
  public MediaList(Users user, String title, String description, List<MediaListItem> mediaListItems)  {
    this.user = user;
    this.title = title;
    this.description = description;
    this.mediaListItems = mediaListItems;
  }

  /**
   * Constructs a new MediaList as a watchlist with the specified user, media IDs,
   * watchlist status and users to share with.
   * 
   * @param user            the user associated with the watchlist
   * @param title           the title of the watchlist
   * @param mediaListItems  a list of {@link MediaListItem} objects to set as the media items of the list.
   * @param watchlist       the watchlist status of the media list
   * @param sharedWith      the list of users the watchlis is shared with
   */
  public MediaList(
    Users user,
    String title,
    List<MediaListItem> mediaListItems,
    boolean watchlist,
    List<Integer> sharedWith
  ) {
    this.user = user;
    this.title = title;
    this.mediaListItems = mediaListItems;
    this.watchlist = watchlist;
    this.sharedWith = sharedWith;
  }

  /**
   * Default constructor for JPA.
   */
  public MediaList()  {
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
   * Gets the user associated with the media list.
   *
   * @return the user associated with the media list
   */
  public Users getUser() {
    return user;
  }

  /**
   * Sets the user associated with the media list.
   *
   * @param user the user associated with the media list
   */
  public void setUser(Users user) {
    this.user = user;
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
   * Retrieves the media items of the list.
   *
   * @return a list of {@link MediaListItem} of the list.
   */
  public List<MediaListItem> getMediaListItems() {
    return mediaListItems;
  }

  /**
   * Sets the media items of the list.
   *
   * @param mediaListItems a list of {@link MediaListItem} objects to set as the media items.
   */
  public void setMediaListItems(List<MediaListItem> mediaListItems) {
    this.mediaListItems = mediaListItems;
  }

  /**
   * Checks if the media list is a watchlist.
   *
   * @return true if the media list is a watchlist, false otherwise
   */
  public boolean isWatchlist() {
    return watchlist;
  }

  /**
   * Sets whether the media list is a watchlist.
   *
   * @param watchlist true if the media list should be a watchlist, false otherwise
   */
  public void setWatchlist(boolean watchlist) {
    this.watchlist = watchlist;
  }

  /**
   * Gets the IDs of the users the media list is shared with.
   * 
   * @return the IDs of the users the media list is shared with.
   */
  public List<Integer> getSharedWith() {
    return sharedWith;
  }

  /**
   * Sets the list of user IDs to share the media list with.
   * 
   * @param sharedWith the list of user IDs to share the media list with.
   */
    public void setSharedWith(List<Integer> sharedWith) {
    this.sharedWith = sharedWith;
  }

  /**
   * Gets the amount of likes by users for the media list.
   * 
   * @return the amount of likes for the media list.
   */
  public long getLikeCount() {
    return likeCount;
  }

  /**
   * Sets the amount of likes by users for the media list.
   * 
   * @param likeCount the amount of likes to set.
   */
  public void setLikeCount(long likeCount) {
    this.likeCount = likeCount;
  }

  /**
   * Gets whether the current user has liked the media list.
   *
   * @return whether the current user has liked the media list
   */
  public boolean getUserHasLiked() {
    return userHasLiked;
  }

  /**
   * Sets whether the current user has liked the media list.
   * 
   * @param userHasLiked whether the current user has liked the media list
   */
  public void setUserHasLiked(boolean userHasLiked) {
    this.userHasLiked = userHasLiked;
  }

  /**
   * Gets the creation timestamp of the media list.
   *
   * @return the creation timestamp of the media list
   */
  public Date getCreatedAt() {
    return createdAt;
  }
}
