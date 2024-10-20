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

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Date createdAt;


  public CompletedChallenge(Users user, Challenge challenge)  {
    this.user = user;
    this.challenge = challenge;
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

  public Users getUser() {
    return user;
  }

  public void setUser(Users user) {
    this.user = user;
  }

  public Challenge getChallenge() {
    return challenge;
  }

  public void setChallenge(Challenge challenge) {
    this.challenge = challenge;
  }

  public Date getCreatedAt() {
    return createdAt;
  }
}
