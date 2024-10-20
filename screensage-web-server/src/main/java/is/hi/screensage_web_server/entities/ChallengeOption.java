package is.hi.screensage_web_server.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "challenge_options")
public class ChallengeOption {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String option;

  @JsonIgnore
  private boolean correct = false;

  @JsonIgnore
  @ManyToOne
  private Challenge challenge;

    /**
   * Constructs a new option for a challenge.
   *
   * @param challenge the challenge this options belongs to
   * @param option the option that can be picked
   */
  public ChallengeOption(Challenge challenge, String option)  {
    this.challenge = challenge;
    this.option = option;
  }

  /**
   * Constructs a new option for a challenge.
   *
   * @param challenge the challenge this options belongs to
   * @param option the option that can be picked
   * @param correct boolean stating whether this option is the correct answer 
   *                for the challenge, true if such, otherwise false
   */
  public ChallengeOption(Challenge challenge, String option, boolean correct)  {
    this.challenge = challenge;
    this.option = option;
    this.correct = correct;
  }

  /**
   * Default constructor for JPA.
   */
  public ChallengeOption()  {
  }

  /**
   * Returns the unique identifier of the challenge option.
   *
   * @return the ID the challenge option
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the challenge that this option belongs to.
   *
   * @return the xhallenge
   */
  public Challenge getChallenge() {
    return challenge;
  }

  /**
   * Sets the challenge that this option belongs to.
   * 
   * @param option the option to be set
   */
  public void setChallenge(Challenge challenge) {
      this.challenge = challenge;
  }

  /**
   * Returns the option that can be picked for the corresponding challenge.
   *
   * @return the option
   */
  public String getOption() {
    return option;
  }

  /**
   * Sets the option that can be picked for the corresponding challenge.
   * 
   * @param option the option to be set
   */
  public void setOption(String option) {
    this.option = option;
  }

  /**
   * Returns whether this options is the correct answer 
   * for the corresponding challenge.
   *
   * @return true is option is correct, otherwise false
   */
  public boolean getCorrect() {
    return correct;
  }

  /**
   * Sets the whether this options is the correct answer 
   * for the corresponding challenge.
   * 
   * @param correct true is option is correct, otherwise false
   */
  public void setCorrect(boolean correct) {
    this.correct = correct;
  }
}
