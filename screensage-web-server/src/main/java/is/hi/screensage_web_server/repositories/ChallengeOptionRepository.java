package is.hi.screensage_web_server.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import is.hi.screensage_web_server.entities.ChallengeOption;

/**
 * Repository interface for managing {@link ChallengeOption} entities in the database.
 */
public interface ChallengeOptionRepository extends JpaRepository<ChallengeOption, Integer> {

  @Query("SELECT co FROM ChallengeOption co WHERE co.challenge.id = :challengeId AND co.correct = true")
  Optional<ChallengeOption> getCorrectOptionForChallenge(int challengeId);

}
