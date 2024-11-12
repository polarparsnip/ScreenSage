package is.hi.screensage_web_server.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import is.hi.screensage_web_server.entities.MediaList;
import is.hi.screensage_web_server.models.MediaListPostRequest;
import is.hi.screensage_web_server.models.MediaListRequest;
import is.hi.screensage_web_server.models.UserPrincipal;
import is.hi.screensage_web_server.services.MediaListService;
import org.springframework.data.domain.Page;

/**
 * REST controller for managing media lists and watchlists.
 */
@RestController
public class MediaListController {

  @Autowired
  private MediaListService mediaListService;

  /**
   * Retrieves a paginated list of media lists for the authenticated user.
   *
   * @param page the page number to retrieve (default is 1)
   * @return     a ResponseEntity containing the paginated list of media lists
   */
  @GetMapping("/lists")
  public ResponseEntity<?> getLists(
    @RequestParam(defaultValue = "1") int page
  ) { 
    int pageSize = 10;
    Page<MediaList> userMediaList = mediaListService.getMediaLists(page, pageSize);
    return ResponseEntity.ok(userMediaList);
  }

  /**
   * Creates a new media list for the authenticated user.
   *
   * @param mediaListRequest the request payload containing media list details
   * @return                 a ResponseEntity containing the newly created media list
   * @throws Exception       if an error occurs during list creation
   */
  @PostMapping("/lists")
  public ResponseEntity<?> postList(
    @RequestBody MediaListPostRequest mediaListRequest
  ) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();

    MediaList newMediaList = mediaListService.createMediaList(userId, mediaListRequest);
    return ResponseEntity.ok(newMediaList);
  }

  /**
   * Retrieves a specific media list by its ID.
   *
   * @param listId     the ID of the media list to retrieve
   * @return           a ResponseEntity containing the requested media list
   * @throws Exception if an error occurs while retrieving the list
   */
  @GetMapping("/lists/{listId}")
  public ResponseEntity<?> getList(
    @PathVariable int listId
  ) throws Exception { 
    MediaList userMediaList = mediaListService.getMediaList(listId);
    return ResponseEntity.ok(userMediaList);
  }

  /**
   * Updates an existing media list.
   *
   * @param listId           the ID of the media list to update
   * @param mediaListRequest the request payload containing updated media list details
   * @return                 a ResponseEntity containing the updated media list
   * @throws Exception       if an error occurs during list update
   */
  @PatchMapping("/lists/{listId}")
  public ResponseEntity<?> updateList(
    @RequestParam(defaultValue = "false") boolean replace,
    @PathVariable int listId,
    @RequestBody MediaListRequest mediaListRequest
  ) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();

    MediaList updatedMediaList = mediaListService.updateMediaList(listId, userId, mediaListRequest, replace);
    return ResponseEntity.ok(updatedMediaList);
  }

  /**
   * Creates a new wathlist for the authenticated user. 
   * 
   * @param mediaListRequest  the request payload containing watchlist details
   * @return                  a ResponseEntity containging the newly created watchlist
   * @throws Exception        if an error occurs during watchlist creation
   */
  @PostMapping("/watchlists")
  public ResponseEntity<?> postWatchlist(
    @RequestBody MediaListPostRequest mediaListRequest
  ) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();

    MediaList newWatchlist = mediaListService.createMediaList(userId, mediaListRequest);
    return ResponseEntity.ok(newWatchlist);
  }
  
  /**
   * Retrieves a specific watchlist by its ID.
   *
   * @param watchlistId the ID of the watchlist to retrieve
   * @return            a ResponseEntity containing the requested watchlist
   * @throws Exception  if an error occurs while retrieving the watchlist
   */
  @GetMapping("/watchlists/{watchlistId}")
  public ResponseEntity<?> getWatchlist(
    @PathVariable int watchlistId
  ) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();

    MediaList watchlist = mediaListService.getWatchlist(watchlistId, userId);
    return ResponseEntity.ok(watchlist);
  }

/**
   * Updates an existing watchlist.
   *
   * @param watchlistId      the ID of the watchlist to update
   * @param mediaListRequest the request payload containing updated watchlist details
   * @return                 a ResponseEntity containing the updated watchlist
   * @throws Exception       if an error occurs during watchlist update
   */
  @PatchMapping("/watchlists/{watchlistId}")
  public ResponseEntity<?> updateWatchlist(
    @RequestParam(defaultValue = "false") boolean replace,
    @PathVariable int watchlistId,
    @RequestBody MediaListRequest mediaListRequest
  ) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();

    MediaList updatedWatchlist = mediaListService.updateMediaList(watchlistId, userId, mediaListRequest, replace);
    return ResponseEntity.ok(updatedWatchlist);
  }

 /**
   * Updates the list of users with whom the watchlist is shared.
   *
   * @param watchlistId the ID of the watchlist to update
   * @param update      a map containing the user IDs to share the watchlist with
   * @return            a ResponseEntity containing the updated list of shared user IDs
   * @throws Exception  if an error occurs during the update
   */
  @PatchMapping("/watchlists/{watchlistId}/shared_with")
  public ResponseEntity<?> updateSharedWith(
    @PathVariable int watchlistId,
    @RequestBody Map<String, List<Integer>> update
  ) throws Exception {
    if (update.containsKey("sharedWith")) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
      int userId = authenticatedUser.getId();

      List<Integer> userIds = update.get("sharedWith");
      List<Integer> updatedSharedWith = mediaListService.updateWatchlistSharedWith(watchlistId, userId, userIds);
      return ResponseEntity.ok(updatedSharedWith);
    }
    return ResponseEntity.badRequest().body("Bad request");
  }
  
}
