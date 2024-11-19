package is.hi.screensage_web_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import is.hi.screensage_web_server.entities.Quote;

/**
 * Repository interface for managing {@link Challenge} entities in the database.
 */
public interface QuoteRepository extends JpaRepository<Quote, Integer> {
  
  /**
   * Retrieves a random quote from the database.
   *
   * @return a randomly selected {@link Quote} object
   */
  @Query(value = "SELECT * FROM quotes ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
  Quote getRandomQuote();
  
}
