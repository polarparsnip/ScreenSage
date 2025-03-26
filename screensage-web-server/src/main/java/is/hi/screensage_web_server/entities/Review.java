package is.hi.screensage_web_server.entities;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "reviews")
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private int mediaId;
  private String mediaTitle;

  @ManyToOne
  private Users user;

  private double rating;

  private String content;

  private String type;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Date createdAt;


  /**
   * Constructs a new review by a user for a piece of media 
   * with the specified content and rating.
   *
   * @param mediaId the ID of the media the review is for
   * @param user the user who made the review
   * @param content the text content of the review
   * @param rating the rating for the media
   */
  public Review(int mediaId, String mediaTitle, Users user, double rating, String content, String type)  {
    if (type.equals("tv") || type.equals("movie")) {
      this.type = type;
    }
    this.mediaId = mediaId;
    this.mediaTitle = mediaTitle;
    this.user = user;
    this.rating = rating;
    this.content = content;
  }

  /**
   * Default constructor for JPA.
   */
  public Review()  {
  }

  /**
   * Returns the unique identifier of the review.
   *
   * @return the review's ID
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the ID of the media the review was for.
   *
   * @return the media ID
   */
  public int getMediaId() {
    return mediaId;
  }

  /**
   * Sets the ID of the media the review was for.
   * 
   * @param mediaId the media ID to be set
   */
  public void setMediaId(int mediaId) {
    this.mediaId = mediaId;
  }

  /**
   * Returns the title of the media the review was for.
   *
   * @return the media title
   */
  public String getMediaTitle() {
    return mediaTitle;
  }

  /**
   * Sets the title of the media the review was for.
   * 
   * @param mediaTitle the media title to be set
   */
  public void setMediaTitle(String mediaTitle) {
    this.mediaTitle = mediaTitle;
  }

  /**
   * Returns the user who made the review.
   *
   * @return the {@link Users} who made the review
   */
  public Users getUser() {
    return user;
  }

  /**
   * Sets the user who made the review.
   * 
   * @param user the {@link Users} to be set
   */
  public void setUser(Users user) {
    this.user = user;
  }

  /**
   * Returns the rating of the review.
   *
   * @return the review's rating
   */
  public double getRating() {
    return rating;
  }

  /**
   * Sets the rating for the review.
   * 
   * @param rating the rating to be set
   */
  public void setRating(double rating) {
    this.rating = rating;
  }

  /**
   * Returns the written content of the review.
   *
   * @return the review's content
   */
  public String getContent() {
    return content;
  }

  /**
   * Sets the content for the review.
   * 
   * @param content the content to be set
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * Returns the media type of the media the review is for.
   * Either tv or movie.
   *
   * @return the media type
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the media type of the media the review is for.
   * Either tv or movie.
   * 
   * @param type the media type to be set
   */
  public void setType(String type) {
    if (!type.equals("tv") && !type.equals("movie")) {
      return;
    }

    this.type = type;
  }

  /**
   * Returns the creation date of the review.
   *
   * @return the creation date of the review
   */
  public Date getCreatedAt() {
    return createdAt;
  }

}
