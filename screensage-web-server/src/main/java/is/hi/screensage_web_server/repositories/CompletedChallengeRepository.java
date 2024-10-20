package is.hi.screensage_web_server.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.screensage_web_server.entities.CompletedChallenge;

/**
 * Repository interface for managing {@link CompletedChallenge} entities in the database.
 */
public interface CompletedChallengeRepository extends JpaRepository<CompletedChallenge, Integer> {
  

  
}
