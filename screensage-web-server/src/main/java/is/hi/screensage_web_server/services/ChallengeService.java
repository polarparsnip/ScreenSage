package is.hi.screensage_web_server.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import is.hi.screensage_web_server.config.CustomExceptions.ResourceNotFoundException;
import is.hi.screensage_web_server.entities.Challenge;
import is.hi.screensage_web_server.entities.ChallengeOption;
import is.hi.screensage_web_server.entities.CompletedChallenge;
import is.hi.screensage_web_server.entities.Users;
import is.hi.screensage_web_server.interfaces.ChallengeServiceInterface;
import is.hi.screensage_web_server.interfaces.UserServiceInterface;
import is.hi.screensage_web_server.models.ChallengeInfoDTO;
import is.hi.screensage_web_server.models.ChallengeResponse;
import is.hi.screensage_web_server.repositories.ChallengeOptionRepository;
import is.hi.screensage_web_server.repositories.ChallengeRepository;
import is.hi.screensage_web_server.repositories.CompletedChallengeRepository;

@Service
public class ChallengeService implements ChallengeServiceInterface {

  @Autowired
  private ChallengeRepository challengeRepository;

  @Autowired
  private ChallengeOptionRepository challengeOptionRepository;

  @Autowired
  private CompletedChallengeRepository completedChallengeRepository;

  @Lazy
  @Autowired
  private UserServiceInterface userService;

  @Override
  public Challenge getRandomChallenge() {
    Challenge randomChallenge = challengeRepository.getRandomChallenge();
    return randomChallenge;
  };

  @Override
  public ChallengeResponse getChallengeResult(int userId, int challengeId, int optionId) {

    ChallengeOption chosenOption = findChallengeOption(optionId);

    if (chosenOption == null) {
      throw new ResourceNotFoundException("The chosen option could not be found");
    }

    boolean isCorrect = chosenOption.getCorrect();

    if (userId != 0) {
      Users user = userService.findUser(userId);
      Challenge challenge = findChallenge(challengeId);
      int points = isCorrect ? challenge.getPoints() : 0;
      completedChallengeRepository.save(new CompletedChallenge(user, challenge, points));
    }
    
    if (!isCorrect) {
      Optional<ChallengeOption> correctOption = challengeOptionRepository.getCorrectOptionForChallenge(challengeId);
      if (!correctOption.isPresent()) {
        throw new ResourceNotFoundException("The correct option could not be found");
      }
      return new ChallengeResponse(correctOption.get(), isCorrect);
    }

    return new ChallengeResponse(chosenOption, isCorrect);
  }

  @Override
  public Challenge findChallenge(int challengeId) {
    Optional<Challenge> challenge = challengeRepository.findById(challengeId);
    if (challenge.isPresent()) {
      return challenge.get();
    }
    return null;
  }

  @Override
  public ChallengeOption findChallengeOption(int optionId) {
    Optional<ChallengeOption> challengeOption = challengeOptionRepository.findById(optionId);
    if (challengeOption.isPresent()) {
      return challengeOption.get();
    }
    return null;
  }

  @Override
  public ChallengeInfoDTO hasCompletedChallenge(int userId, int challengeId) {
    Users user = userService.findUser(userId);
    Challenge challenge = findChallenge(challengeId);

    boolean hasCompletedChallenge = completedChallengeRepository.existsByUserAndChallenge(user, challenge);

    ChallengeInfoDTO challengeInfo = new ChallengeInfoDTO(user, challenge, hasCompletedChallenge);
    return challengeInfo;
  }
}
