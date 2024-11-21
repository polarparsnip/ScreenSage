package is.hi.screensage_web_server.interfaces;

import is.hi.screensage_web_server.entities.Challenge;
import is.hi.screensage_web_server.entities.ChallengeOption;
import is.hi.screensage_web_server.models.ChallengeInfoDTO;
import is.hi.screensage_web_server.models.ChallengeResponse;

public interface ChallengeServiceInterface {
  /**
   * Retrieves a random challenge.
   *
   * @return a randomly selected {@link Challenge} object
   */
  public Challenge getRandomChallenge();

  /**
   * Gets the result of a challenge attempt by a specific user.
   *
   * @param  userId the ID of the user attempting the challenge
   * @param  challengeId the ID of the challenge being attempted
   * @param  optionId the ID of the selected option in the challenge
   * @return a {@link ChallengeResponse} object containing the result of the challenge attempt
   */
  public ChallengeResponse getChallengeResult(int userId, int challengeId, int optionId);

  /**
   * Finds and retrieves a challenge by its ID.
   *
   * @param  challengeId the ID of the challenge to retrieve
   * @return the {@link Challenge} object for the specified challenge ID, or {@code null} if not found
   */
  public Challenge findChallenge(int challengeId);

  /**
   * Finds and retrieves an option for a specific challenge.
   *
   * @param  challengeId the ID of the challenge whose option is to be retrieved
   * @return the {@link ChallengeOption} object associated with the specified challenge ID, or {@code null} if not found
   */
  public ChallengeOption findChallengeOption(int challengeId);

  /**
   * Checks whether a user has completed a specific challenge.
   *
   * @param userId      the ID of the user making the request
   * @param challengeId the ID of the challenge being checked
   * @return            {@link ChallengeInfoDTO} object containing 
   *                    {@code true} if the specified user has completed the challenge, 
   *                    otherwise {@code false}
   */
  public ChallengeInfoDTO hasCompletedChallenge(int userId, int challengeId);

}
