package is.hi.screensage_web_server.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.screensage_web_server.entities.Challenge;
import is.hi.screensage_web_server.entities.CompletedChallenge;
import is.hi.screensage_web_server.entities.Users;

/**
 * Repository interface for managing {@link CompletedChallenge} entities in the database.
 */
public interface CompletedChallengeRepository extends JpaRepository<CompletedChallenge, Integer> {
  
  /**
   * Checks if a {@link CompletedChallenge} entry exists for the given user and challenge.
   *
   * @param  user the {@link Users} entity representing the user.
   * @param  challenge the {@link Challenge} entity representing the challenge.
   * @return {@code true} if a {@link CompletedChallenge} entry exists for the specified user and challenge,
   *         {@code false} otherwise.
   */
  boolean existsByUserAndChallenge(Users user, Challenge challenge);
  
}
