package is.hi.screensage_web_server.models;

import is.hi.screensage_web_server.entities.Challenge;
import is.hi.screensage_web_server.entities.Users;

  /**
   * DTO for completed challenge information.
   */
public class ChallengeInfoDTO {
  private Users user;
  private Challenge challenge;
  private boolean hasCompleted;

  /**
   * Constructs a new DTO with the specified user information and completed challenge information.
   *
   * @param user         the user information
   * @param challenge    the challenge
   * @param hasCompleted whether the user has completed the queried challenge
   */
  public ChallengeInfoDTO(Users user, Challenge challenge, boolean hasCompleted) {
    this.user = user;
    this.challenge = challenge;
    this.hasCompleted = hasCompleted;
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

  public boolean getCompleted() {
    return hasCompleted;
  }

  public void setCompleted(boolean hasCompleted) {
    this.hasCompleted = hasCompleted;
  }

}
