package is.hi.screensage_web_server.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "quotes")
public class Quote {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String text;
  private String title;
  private int year;
  private int mediaId;
  private String type;

  /**
   * Constructs a new media quote.
   *
   * @param text    the text content of the quote
   * @param year    the title of the media the quote is from.
   * @param year    the release year of the media the quote is from.
   * @param type    the type of the media (e.g., "movies", "shows", or "anime").
   * @param mediaId the ID of the media the quote is from
   */
  public Quote(String text, String title, int year, String type, int mediaId)  {
    this.text = text;
    this.title = title;
    this.year = year;
    this.type = type;
    this.mediaId = mediaId;
  }

  /**
   * Default constructor for JPA.
   */
  public Quote()  {
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
   * Sets the text content of the quote
   *
   * @param text the text content to set for the quote
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Returns the text content of the quote.
   *
   * @return the text content of the quote
   */
  public String getText() {
    return text;
  }

  /**
   * Gets the title of the media associated with the quote.
   *
   * @return the title of the media.
   */
  public String getTitle() {
    return title;
  }

  /**
  * Sets the title of the media associated with the quote.
  *
  * @param title the title of the media to set.
  */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
  * Gets the release year of the media associated with the quote.
  *
  * @return the release year of the media.
  */
  public int getYear() {
    return year;
  }

  /**
  * Sets the release year of the media associated with the quote.
  *
  * @param year the release year of the media to set.
  */
  public void setYear(int year) {
    this.year = year;
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
   * Gets the unique identifier for the media the quote is from.
   *
   * @return the media ID
   */
  public int getMediaId() {
    return mediaId;
  }

  /**
   * Sets the unique identifier for the media the quote is from.
   *
   * @param mediaId the media ID to set
   */
  public void setMediaId(int mediaId) {
    this.mediaId = mediaId;
  }

}
