package is.hi.screensage_web_server.repositories;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import is.hi.screensage_web_server.entities.MediaList;
import is.hi.screensage_web_server.models.MediaListConcise;

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
  @Query("SELECT ml FROM MediaList ml WHERE ml.user.id = :userId AND ml.watchlist = FALSE ORDER BY ml.createdAt DESC, ml.title ASC")
  Page<MediaList> getUserMediaLists(int userId, Pageable pageable);
  
  /**
   * Retrieves a paginated list of watchlists associated with a specific user,
   * where the watchlist flag is true, ordered by the creation date in descending order.
   *
   * @param userId   the ID of the user whose watchlists are to be retrieved
   * @param pageable the pagination information including page number and size
   * @return         a Page of MediaList objects for the specified user's watchlists
   */
  @Query("SELECT ml FROM MediaList ml WHERE ml.user.id = :userId AND ml.watchlist = TRUE ORDER BY ml.createdAt DESC, ml.title ASC")
  Page<MediaList> getUserWatchlists(int userId, Pageable pageable);

  /**
   * Retrieves a concise list of media lists for a specific user, filtered by watchlist status.
   * 
   * This method queries the database to find all {@link MediaListConcise} records associated
   * with a given user ID, filtered by whether or not the media lists are designated as a watchlist.
   * Each result includes the media list's ID, title, type, watchlist status, and sharedWith property.
   * 
   * @param userId    the ID of the user for whom to retrieve media lists
   * @param watchlist a boolean flag indicating whether to retrieve media lists marked as watchlists {@code true}
   *                  or non-watchlists {@code false}
   * @return          a list of {@link MediaListConcise} objects containing selected fields from {@link MediaList}
   */
  @Query(
    "SELECT new is.hi.screensage_web_server.models.MediaListConcise(ml.id, ml.title, ml.type, ml.watchlist, ml.sharedWith) " +
    "FROM MediaList ml WHERE ml.user.id = :userId AND ml.watchlist = :watchlist"
  )
  List<MediaListConcise> findAllByUserIdAndWatchlist(@Param("userId") Integer userId, @Param("watchlist") boolean watchlist);
  
}