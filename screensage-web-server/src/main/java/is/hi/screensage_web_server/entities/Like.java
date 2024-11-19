package is.hi.screensage_web_server.entities;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "likes")
public class Like {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  private Users user;

  @ManyToOne
  private MediaList mediaList;

  private String type;
  private int mediaId;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Date createdAt;

  /**
   * Constructs a new like for a media list.
   *
   * @param user the user who created the like
   * @param list the media list the like is for
   */
  public Like(Users user, MediaList mediaList)  {
    this.user = user;
    this.mediaList = mediaList;
  }

  /**
   * Constructs a new like for a media item.
   *
   * @param user    the user who created the like
   * @param type    the type of the media the like is for (e.g., "movies", "shows", or "anime").
   * @param mediaId the ID of the media the like is for
   */
  public Like(Users user, String type, int mediaId)  {
    this.user = user;
    this.type = type;
    this.mediaId = mediaId;
  }

  /**
   * Default constructor for JPA.
   */
  public Like()  {
  }

  /**
   * Returns the unique identifier of the challenge.
   *
   * @return the ID the challenge
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the user who made the like.
   *
   * @return the {@link Users} who made the like
   */
  public Users getUser() {
    return user;
  }

  /**
   * Sets the user who made the like.
   * 
   * @param user the {@link Users} to be set
   */
  public void setUser(Users user) {
    this.user = user;
  }

  /**
   * Gets the media list for which the like is for.
   *
   * @return the media list
   */
  public MediaList getMediaList() {
    return mediaList;
  }

  /**
   * Sets the media list for which the like is for.
   *
   * @param mediaList the media list to set
   */
  public void setMediaList(MediaList mediaList) {
    this.mediaList = mediaList;
  }

  /**
   * Returns the media type of the media the quote is from.
   * Either shows, anime or movies.
   *
   * @return the media type
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the media type of the media the quote is from.
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

  /**
   * Gets the unique identifier for the media for which the like is for.
   *
   * @return the media ID
   */
  public int getMediaId() {
    return mediaId;
  }

  /**
   * Sets the unique identifier for the media for which the like is for.
   *
   * @param mediaId the media ID to set
   */
  public void setMediaId(int mediaId) {
    this.mediaId = mediaId;
  }

  /**
   * Returns the creation date of the like.
   *
   * @return the creation date of the like
   */
  public Date getCreatedAt() {
    return createdAt;
  }

}
