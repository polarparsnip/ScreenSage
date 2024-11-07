package is.hi.screensage_web_server.interfaces;

import org.springframework.data.domain.Page;

import is.hi.screensage_web_server.entities.MediaList;
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
  public MediaList createMediaList(int userId, MediaListRequest mediaListRequest) throws Exception;

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
   * @return                 the updated MediaList object
   * @throws Exception       if an error occurs during the update
   */
  public MediaList updateMediaList(int listId, int userId, MediaListRequest mediaListRequest) throws Exception;

  /**
   * Retrieves a paginated list of media lists for a specific user.
   *
   * @param userId   the ID of the user whose media lists are to be retrieved
   * @param page     the page number to retrieve
   * @param pageSize the number of entries per page
   * @return         a Page of MediaList objects for the specified user
   */
  public Page<MediaList> getUserMediaLists(int userId, int page, int pageSize);

}
