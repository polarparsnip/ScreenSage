package is.hi.screensage_web_server.entities;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents an item in a media list, with details about the media such as title, ID, and image URL.
 */
@Entity
@Table(name = "media_list_items")
public class MediaListItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @JsonIgnore
  @ManyToOne
  private MediaList mediaList;

  private int mediaId;
  private String mediaTitle;
  private String mediaSummary;
  private String mediaImg;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Date createdAt;

  /**
   * Constructor for creating a new MediaListItem.
   *
   * @param mediaList    the MediaList that this item belongs to
   * @param mediaId      the unique identifier for the media
   * @param mediaTitle   the title of the media
   * @param mediaSummary the summary of the media
   * @param mediaImg     the URL of the media image
   */
  public MediaListItem(
    MediaList mediaList, 
    int mediaId, 
    String mediaTitle, 
    String mediaSummary, 
    String mediaImg
  )  {
    this.mediaList = mediaList;
    this.mediaId = mediaId;
    this.mediaTitle = mediaTitle;
    this.mediaSummary = mediaSummary;
    this.mediaImg = mediaImg;
  }

  /**
   * Default constructor for JPA.
   */
  public MediaListItem()  {
  }

  /**
   * Gets the unique identifier of the media list item.
   *
   * @return the unique identifier of the media list item
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the media list that this item belongs to.
   *
   * @return the {@link MediaList} that this item belongs to
   */
  public MediaList getMediaList() {
    return mediaList;
  }

  /**
   * Sets the media list that this item belongs to.
   *
   * @param mediaList the {@link MediaList} to set
   */
  public void setMediaList(MediaList mediaList) {
    this.mediaList = mediaList;
  }

  /**
   * Gets the unique identifier for the media.
   *
   * @return the media ID
   */
  public int getMediaId() {
    return mediaId;
  }

  /**
   * Sets the unique identifier for the media.
   *
   * @param mediaId the media ID to set
   */
  public void setMediaId(int mediaId) {
    this.mediaId = mediaId;
  }

  /**
   * Gets the title of the media.
   *
   * @return the media title
   */
  public String getMediaTitle() {
    return mediaTitle;
  }

  /**
   * Sets the title of the media.
   *
   * @param mediaTitle the media title to set
   */
  public void setMediaTitle(String mediaTitle) {
    this.mediaTitle = mediaTitle;
  }

  /**
   * Gets the sumary of the media.
   *
   * @return the media summary
   */
  public String getMediaSummary() {
    return mediaSummary;
  }

  /**
   * Sets the summary of the media.
   *
   * @param mediaSummary the media summary to set
   */
  public void setMediaSummary(String mediaSummary) {
    this.mediaSummary = mediaSummary;
  }

  /**
   * Gets the URL of the media image.
   *
   * @return the media image URL
   */
  public String getMediaImg() {
    return mediaImg;
  }

  /**
   * Sets the URL of the media image.
   *
   * @param mediaImg the media image URL to set
   */
  public void setMediaImg(String mediaImg) {
    this.mediaImg = mediaImg;
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
