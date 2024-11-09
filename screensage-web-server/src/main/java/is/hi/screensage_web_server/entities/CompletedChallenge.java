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
@Table(name = "completed_challenges")
public class CompletedChallenge {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  
  @ManyToOne
  private Users user;
  
  @ManyToOne
  private Challenge challenge;

  private int points;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Date createdAt;

  /**
   * Constructs a new {@link CompletedChallenge} with the specified user and challenge.
   *
   * @param user      the {@link Users} object representing the user who completed the challenge
   * @param challenge the {@link Challenge} object representing the challenge that was completed
   * @param points    the points awarded for answering the challege correctly, 0 points if answered incorrectly
   */
  public CompletedChallenge(Users user, Challenge challenge, int points)  {
    this.user = user;
    this.challenge = challenge;
    this.points = points;
  }

  /**
   * Default constructor for JPA.
   */
  public CompletedChallenge()  {
  }

  /**
   * Returns the unique identifier of the completed challenge.
   *
   * @return the review's ID
   */
  public int getId() {
    return id;
  }

  /**
   * Retrieves the user who completed the challenge.
   *
   * @return the {@link Users} object representing the user
   */
  public Users getUser() {
    return user;
  }

  /**
   * Sets the user who completed the challenge.
   *
   * @param user the {@link Users} object representing the user to be set
   */
  public void setUser(Users user) {
    this.user = user;
  }

  /**
   * Retrieves the challenge that was completed.
   *
   * @return the {@link Challenge} object representing the completed challenge
   */
  public Challenge getChallenge() {
    return challenge;
  }

  /**
   * Sets the challenge that was completed.
   *
   * @param challenge the {@link Challenge} object to be set as the completed challenge
   */
  public void setChallenge(Challenge challenge) {
    this.challenge = challenge;
  }

  /**
   * Gets the points awarded for the completed challenge.
   *
   * @return the points awarded
   */
  public int getPoints() {
    return points;
  }

  /**
   * Sets the points awarded for the completed challenge.
   *
   * @param points the points to set
   */
  public void setPoints(int points) {
    this.points = points;
  }

  /**
   * Retrieves the date and time when the challenge was completed.
   *
   * @return the {@link Date} object representing the completion timestamp
   */
  public Date getCreatedAt() {
    return createdAt;
  }
}
