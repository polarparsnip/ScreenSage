package is.hi.screensage_web_server.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;

import is.hi.screensage_web_server.entities.MediaList;
import is.hi.screensage_web_server.models.MediaListConcise;
import is.hi.screensage_web_server.models.MediaListPostRequest;
import is.hi.screensage_web_server.models.MediaListRequest;

/**
 * MediaServiceInterface defines the contract for media list related services, 
 * including watchlists.
 * 
 * Provides methods to retrieve media lists, make lists, and update lists.
 */
public interface MediaListServiceInterface {

  /**
   * Retrieves a media list by its ID.
   *
   * @param listId the ID of the media list to retrieve
   * @return       the MediaList object corresponding to the specified ID
   */
  public MediaList findMediaList(int listId);

  /**
   * Retrieves a paginated list of media lists.
   *
   * @param page     the page number to retrieve
   * @param pageSize the number of entries per page
   * @return         a Page of MediaList objects
   */
  public Page<MediaList> getMediaLists(int page, int pageSize);

  /**
   * Creates a new media list for a user based on the provided request.
   *
   * @param userId           the ID of the user creating the media list
   * @param mediaListRequest the request object containing media list details
   * @return                 the newly created MediaList object
   * @throws Exception       if an error occurs during creation
   */
  public MediaList createMediaList(int userId, MediaListPostRequest mediaListRequest) throws Exception;

  /**
   * Retrieves a media list by its ID.
   *
   * @param listId     the ID of the media list to retrieve
   * @return           the MediaList object corresponding to the specified ID
   * @throws Exception if the media list is not found or another error occurs
   */
  public MediaList getMediaList(int listId) throws Exception;

  /**
   * Updates an existing media list.
   *
   * @param listId           the ID of the media list to update
   * @param userId           the ID of the user making the update
   * @param mediaListRequest the request object containing updated media list details
   * @param replace          boolean stating whether the media list items should replace the existing ones,
   *                         {@code true} if they should be replaced, otherwise {@code false}
   * @return                 the updated MediaList object
   * @throws Exception       if an error occurs during the update
   */
  public MediaList updateMediaList(int listId, int userId, MediaListRequest mediaListRequest, boolean replace) throws Exception;

  /**
   * Retrieves a paginated list of media lists for a specific user.
   *
   * @param userId   the ID of the user whose media lists are to be retrieved
   * @param page     the page number to retrieve
   * @param pageSize the number of entries per page
   * @return         a Page of MediaList objects for the specified user
   */
  public Page<MediaList> getUserMediaLists(int userId, int page, int pageSize);

  /**
   * Retrieves a specific watchlist for a user by its ID.
   *
   * @param watchlistId the ID of the watchlist to retrieve
   * @param userId      the ID of the user requesting the watchlist
   * @return            the MediaList object representing the user's watchlist
   * @throws Exception  if the watchlist is not found or another error occurs
   */
  public MediaList getWatchlist(int watchlistId, int userId) throws Exception;

  /**
   * Retrieves a paginated list of watchlists for a specific user.
   * 
   * @param userId   the ID of the user whose watchlists are to be retrieved
   * @param page     the page number to retrieve
   * @param pageSize the number of entries per page
   * @return         a page of MediaList objects for the specified user's watchlists
   */
  public Page<MediaList> getUserWatchlists(int userId, int page, int pageSize);

  /**
   * Updates the list of user IDs with whom a watchlist is shared.
   * 
   * @param watchlistId the ID of the watchlist to update
   * @param userId      the ID of the user making the update
   * @param userIds     the list of user IDs that the watchlist is to be shared with
   * @return            a list of updated user IDs that the watchlist is shared with
   * @throws Exception  if an error occurs during the update
   */
  public List<Integer> updateWatchlistSharedWith(int wathclistId, int userId, List<Integer> userIds) throws Exception;

  /**
   * Retrieves a list of concise media lists for a specific user.
   *
   * This method calls the repository to fetch media lists associated with the specified user ID
   * and filtered by the watchlist flag. The method returns each media list's essential details
   * in a {@link MediaListConcise} object, including the ID, title, type, watchlist status, and sharedWith field.
   *
   * @param userId     the ID of the user whose media lists are to be retrieved
   * @param watchlist  a boolean indicating whether to retrieve only watchlists {@code true}
   *                   or regular media lists {@code false}
   * @return           a list of {@link MediaListConcise} objects representing concise information about each media list
   * @throws Exception if an error occurs while retrieving the data
   */
  public List<MediaListConcise> getAllUserMediaListsConcise(int userId, boolean watchlist) throws Exception;
}
