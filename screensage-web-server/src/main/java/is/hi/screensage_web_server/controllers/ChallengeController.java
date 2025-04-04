package is.hi.screensage_web_server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import is.hi.screensage_web_server.entities.Challenge;
import is.hi.screensage_web_server.interfaces.ChallengeServiceInterface;
import is.hi.screensage_web_server.models.ChallengeInfoDTO;
import is.hi.screensage_web_server.models.ChallengeResponse;
import is.hi.screensage_web_server.models.UserPrincipal;

/**
 * REST controller for managing challenge-related operations, such as getting the random daily challenge.
 */
@RestController
public class ChallengeController {

  @Autowired
  private ChallengeServiceInterface challengeService;

  /**
   * Retrieves the daily challenge for the user.
   *
   * @return a {@link ResponseEntity} containing the daily challenge details
   */
  @GetMapping("/challenge")
  public ResponseEntity<?> getDailyChallenge() { 
    Challenge dailyChallenge = challengeService.getRandomChallenge();
    return ResponseEntity.ok(dailyChallenge);
  }

  /**
   * Submits an answer for the daily challenge.
   *
   * @param challengeId the ID of the challenge to be answered
   * @param optionId the ID of the option selected as the answer (required)
   * @param userId the ID of the user submitting the answer (optional, defaults to 0 if not provided)
   * @return a {@link ResponseEntity} indicating the result of the answer submission
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

  /**
   * Checks whether a user has completed a specific challenge.
   *
   * @return a {@link ResponseEntity} containing whether the request user 
   *         has completed the specified challenge
   */
  @GetMapping("/challenge/{id}/has-completed")
  public ResponseEntity<?> hasCompletedChallenge(
    @PathVariable("id") int challengeId
  ) { 
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();

    ChallengeInfoDTO challengeInfo = challengeService.hasCompletedChallenge(userId, challengeId);
    return ResponseEntity.ok(challengeInfo);
  }

}
