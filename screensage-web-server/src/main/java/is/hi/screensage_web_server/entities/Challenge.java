package is.hi.screensage_web_server.entities;

import java.util.List;

import is.hi.screensage_web_server.models.ChallengeType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "challenges")
public class Challenge {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String question;
  
  private String instructions;

  private String image;

  @Enumerated(EnumType.STRING)
  private ChallengeType type;

  @OneToMany(mappedBy = "challenge", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<ChallengeOption> options;


  /**
   * Constructs a new daily challenge.
   *
   * @param question the question for the challenge
   */
  public Challenge(String question, ChallengeType type)  {
    this.question = question;
    this.type = type;
  }

  /**
   * Default constructor for JPA.
   */
  public Challenge()  {
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
   * Sets the question of the challenge.
   *
   * @param question the question to be set for the challenge
   */
  public void setQuestion(String question) {
    this.question = question;
  }

  /**
   * Returns the question of the challenge.
   *
   * @return the question of the challenge
   */
  public String getQuestion() {
    return question;
  }

  /**
   * Sets the instructions of the challenge.
   *
   * @param instructions the instructions to be set for the challenge
   */
  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  /**
   * Returns the instructions of the challenge.
   *
   * @return the instructions of the challenge
   */
  public String getInstructions() {
    return instructions;
  }

  /**
   * Sets the image of the challenge.
   *
   * @param image the image url to set for the challenge
   */
  public void setImage(String image) {
    this.image = image;
  }

  /**
   * Returns the image of the challenge.
   *
   * @return the image of the challenge
   */
  public String getImage() {
    return image;
  }

  /**
   * Returns the type of the challenge.
   *
   * @return the type of the challenge, represented as a {@link ChallengeType} enum
   */
  public ChallengeType getType() {
    return type;
  }

  /**
   * Sets the type of the challenge.
   *
   * @param type the type to set for the challenge, represented as a {@link ChallengeType} enum
   */
  public void setType(ChallengeType type) {
    this.type = type;
  }

  /**
   * Returns the answer options of the challenge.
   *
   * @return the options of the challenge
   */
  public List<ChallengeOption> getOptions() {
    return options;
  }

  /**
   * Sets the answer options of the challenge.
   *
   * @param options the options to set for the challenge
   */
  public void setOptions(List<ChallengeOption> options) {
    this.options = options;
  }
}
