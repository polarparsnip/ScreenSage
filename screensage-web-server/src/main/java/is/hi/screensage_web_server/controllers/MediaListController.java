package is.hi.screensage_web_server.controllers;

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
    int pageSize = 20;
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
    @RequestBody MediaListRequest mediaListRequest
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
    @PathVariable int listId,
    @RequestBody MediaListRequest mediaListRequest
  ) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();

    MediaList updatedMediaList = mediaListService.updateMediaList(listId, userId, mediaListRequest);
    return ResponseEntity.ok(updatedMediaList);
  }

}
