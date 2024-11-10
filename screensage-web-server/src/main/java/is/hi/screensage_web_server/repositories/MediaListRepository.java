package is.hi.screensage_web_server.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import is.hi.screensage_web_server.entities.MediaList;

/**
 * Repository interface for managing {@link MediaList} entities in the database.
 */
public interface MediaListRepository extends JpaRepository<MediaList, Integer> {

  /**
   * Retrieves a paginated list of media lists where the watchlist flag is false,
   * ordered by the creation date in descending order.
   *
   * @param pageable the pagination information including page number and size
   * @return         a Page of MediaList objects where the watchlist is false
   */
  Page<MediaList> findAllByWatchlistFalseOrderByCreatedAtDesc(Pageable pageable);
  
  /**
   * Retrieves a paginated list of media lists associated with a specific user,
   * ordered by the creation date in descending order.
   *
   * @param userId   the ID of the user whose media lists are to be retrieved
   * @param pageable the pagination information including page number and size
   * @return         a Page of MediaList objects for the specified user
   */
  @Query("SELECT ml FROM MediaList ml WHERE ml.user.id = :userId ORDER BY ml.createdAt DESC")
  Page<MediaList> getUserMediaLists(int userId, Pageable pageable);
  
  /**
   * Retrieves a paginated list of watchlists associated with a specific user,
   * where the watchlist flag is true, ordered by teh creation date in descending order.
   * 
   * @param userId  the ID of the user whose watchlists are to be retrieved
   * @param pageable the pagination information including page number and size
   * @return    a page of MediaLists objects for the specified user's watchlists
   */
  @Query("SELECT ml FROM MediaList ml WHERE ml.user.id = :userID AND ml.watchlist = TRUE ORDER BY ml.createdAT DESC")
  Page<MediaList> getUserWatchlists(int userId, Pageable pageable);
  
}
