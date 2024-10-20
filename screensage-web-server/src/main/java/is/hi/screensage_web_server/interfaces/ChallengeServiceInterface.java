package is.hi.screensage_web_server.interfaces;

import is.hi.screensage_web_server.entities.Challenge;
import is.hi.screensage_web_server.entities.ChallengeOption;
import is.hi.screensage_web_server.models.ChallengeResponse;

public interface ChallengeServiceInterface {
  
  public Challenge getRandomChallenge();

  public ChallengeResponse getChallengeResult(int userId, int challengeId, int optionId);

  public Challenge findChallenge(int challengeId);

  public ChallengeOption findChallengeOption(int challengeId);

}
