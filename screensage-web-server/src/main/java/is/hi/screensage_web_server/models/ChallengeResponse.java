package is.hi.screensage_web_server.models;

import is.hi.screensage_web_server.entities.ChallengeOption;

/**
 * Represents the response of a challenge attempt, 
 * including the correct option and the user's answer status.
 */
public class ChallengeResponse {
  private ChallengeOption correctOption;
  private boolean answeredCorrectly;

  /**
   * Constructs a new {@link ChallengeResponse} with the specified correct option and answer status.
   *
   * @param correctOption the {@link ChallengeOption} that is correct for the challenge
   * @param answeredCorrectly {@code true} if the user answered correctly; {@code false} otherwise
   */
  public ChallengeResponse(ChallengeOption correctOption, boolean answeredCorrectly) {
    this.correctOption = correctOption;
    this.answeredCorrectly = answeredCorrectly;
  }

  /**
   * Retrieves the correct option for the challenge.
   *
   * @return the {@link ChallengeOption} that is correct for the challenge
   */
  public ChallengeOption getCorrectOption() {
    return correctOption;
  }

  /**
   * Sets the correct option for the challenge.
   *
   * @param correctOption the {@link ChallengeOption} to be set as the correct option
   */
  public void setCorrectOption(ChallengeOption correctOption) {
    this.correctOption = correctOption;
  }

  /**
   * Retrieves the user's answer status.
   *
   * @return {@code true} if the user answered correctly; {@code false} otherwise
   */
  public boolean getAnsweredCorrectly() {
    return answeredCorrectly;
  }

  /**
   * Sets the user's answer status.
   *
   * @param answeredCorrectly {@code true} if the user answered correctly; {@code false} otherwise
   */
  public void setAnsweredCorrectly(boolean answeredCorrectly) {
    this.answeredCorrectly = answeredCorrectly;
  }
}