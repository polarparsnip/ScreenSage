package is.hi.screensage_web_server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import is.hi.screensage_web_server.entities.Challenge;
import is.hi.screensage_web_server.interfaces.ChallengeServiceInterface;
import is.hi.screensage_web_server.models.ChallengeResponse;

/**
 * REST controller for managing challenge-related operations, such as getting the random daily challenge.
 */
@RestController
public class ChallengeController {

  @Autowired
  private ChallengeServiceInterface challengeService;

  /**
   * Retrieves a random challenge to be the daily challenge.
   *
   * @return a ResponseEntity containing a random challenge.
   */
  @GetMapping("/challenge")
  public ResponseEntity<?> getDailyChallenge() { 
    Challenge dailyChallenge = challengeService.getRandomChallenge();
    return ResponseEntity.ok(dailyChallenge);
  }

  /**
   * Submits a user's answer for a daily challenge.
   *
   * @return a ResponseEntity containing the result of whether the user answered correctly or not.
   */
  @PostMapping("/challenge/{id}")
  public ResponseEntity<?> postDailyChallengeAnswer(
    @PathVariable("id") int challengeId,
    @RequestParam(name = "optionId", required = true) int optionId,
    @RequestParam(name = "user", required = false, defaultValue = "0") int userId
  ) { 
    ChallengeResponse challengeResult = challengeService.getChallengeResult(userId, challengeId, optionId);
    return ResponseEntity.ok(challengeResult);
  }
}
