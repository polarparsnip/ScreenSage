package is.hi.screensage_web_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import is.hi.screensage_web_server.entities.Challenge;

/**
 * Repository interface for managing {@link Challenge} entities in the database.
 */
public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {
  
  /**
   * Retrieves a random challenge from the database.
   *
   * @return a randomly selected {@link Challenge} object
   */
  @Query(value = "SELECT * FROM challenges ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
  Challenge getRandomChallenge();
  
}
