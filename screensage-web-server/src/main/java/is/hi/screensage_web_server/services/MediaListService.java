package is.hi.screensage_web_server.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import is.hi.screensage_web_server.config.CustomExceptions.ResourceNotFoundException;
import is.hi.screensage_web_server.config.CustomExceptions.UnauthorizedException;
import is.hi.screensage_web_server.entities.MediaList;
import is.hi.screensage_web_server.entities.MediaListItem;
import is.hi.screensage_web_server.entities.Users;
import is.hi.screensage_web_server.interfaces.MediaListServiceInterface;
import is.hi.screensage_web_server.interfaces.UserServiceInterface;
import is.hi.screensage_web_server.models.MediaListItemRequest;
import is.hi.screensage_web_server.models.MediaListRequest;
import is.hi.screensage_web_server.repositories.MediaListRepository;
import jakarta.transaction.Transactional;

@Service
public class MediaListService implements MediaListServiceInterface {
  @Autowired
  private MediaListRepository mediaListRepository;

  @Lazy
  @Autowired
  private UserServiceInterface userService;


  @Override
  public MediaList findMediaList(int listId) {
    Optional<MediaList> mediaList = mediaListRepository.findById(listId);
    if (mediaList.isPresent()) {
      return mediaList.get();
    }
    return null;
  }


  @Override
  public Page<MediaList> getMediaLists(int page, int pageSize) {
    PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
  
    Page<MediaList> mediaLists;
    try {
      mediaLists = mediaListRepository.findAllByWatchlistFalseOrderByCreatedAtDesc(pageRequest);
    } catch (Exception e) {
      System.out.println("Could not find media lists: " + e.getMessage());
      throw new ResourceNotFoundException("Could not find media lists.");
    }
  
    return mediaLists;
  }

  @Override
  public MediaList createMediaList(int userId, MediaListRequest mediaListRequest) throws Exception {
    Users user = userService.findUser(userId);
    if (user == null) {
      System.out.println("Error: User not found");
      throw new ResourceNotFoundException("User not found");
    }

    String type = mediaListRequest.getType();

    if (!type.equals("tv") && !type.equals("movie")) {
      System.out.println("Media type is missing or incorrect.");
      throw new Exception("Media type is missing or incorrect.");
    }

    MediaList newMediaList = new MediaList(user, type);

    boolean isWatchlist = mediaListRequest.isWatchlist();
    if (isWatchlist) {
      newMediaList.setWatchlist(isWatchlist);
    }

    List<Integer> sharedWith = mediaListRequest.getSharedWith(); 
    if (sharedWith != null && !sharedWith.isEmpty()) {
      newMediaList.setSharedWith(sharedWith);
    }

    List<MediaListItem> newMediaListItems = new ArrayList<>();

    List<MediaListItemRequest> mediaListItems = mediaListRequest.getMediaListItems();
    for (MediaListItemRequest item : mediaListItems) {
      MediaListItem newMediaListItem = new MediaListItem(
        newMediaList, 
        item.getMediaId(), 
        item.getMediaTitle(), 
        item.getMediaSummary(),
        item.getMediaImg()
      );
      newMediaListItems.add(newMediaListItem);
    }

    newMediaList.setMediaListItems(newMediaListItems);

    try {
      mediaListRepository.save(newMediaList);
    } catch (Exception e) {
      System.out.println("Error occured while saving media list: " + e.getMessage());
      throw new Exception("Error occurred while saving media list.");
    }

    return newMediaList;
  }

  @Override
  public MediaList getMediaList(int listId) throws Exception {
    MediaList mediaList;
    try {
      mediaList = findMediaList(listId);
    } catch (Exception e) {
      System.out.println("Could not find media list: " + e.getMessage());
      throw new ResourceNotFoundException("Could not find media list.");
    }

    if (mediaList.isWatchlist()) {
      throw new Exception("Cannot fetch list because it is a watchlist");
    }

    return mediaList;
  }

  @Transactional
  @Override
  public MediaList updateMediaList(
    int listId, 
    int userId, 
    MediaListRequest mediaListRequest
  ) throws Exception {

    Users user = userService.findUser(userId);
    if (user == null) {
      System.out.println("Error: User not found");
      throw new ResourceNotFoundException("User not found");
    }

    MediaList mediaList = findMediaList(listId);
    if (mediaList == null) {
      System.out.println("Error: Media list not found");
      throw new ResourceNotFoundException("Media list not found");
    }

    Users listAuthor = mediaList.getUser();

    if (userId != listAuthor.getId()) {
      throw new UnauthorizedException("You do not have permission to edit this list");
    }

    List<MediaListItem> newMediaListItems = new ArrayList<>();

    List<MediaListItemRequest> mediaListItems = mediaListRequest.getMediaListItems();
    for (MediaListItemRequest item : mediaListItems) {
      MediaListItem newMediaListItem = new MediaListItem(
        mediaList, 
        item.getMediaId(), 
        item.getMediaTitle(), 
        item.getMediaSummary(),
        item.getMediaImg()
      );
      newMediaListItems.add(newMediaListItem);
    }

    mediaList.setMediaListItems(newMediaListItems);

    try {
      mediaListRepository.save(mediaList);
    } catch (Exception e) {
      System.out.println("Error occured while updating media list: " + e.getMessage());
      throw new Exception("Error occurred while updating media list.");
    }

    return mediaList;
  }

  @Override
  public Page<MediaList> getUserMediaLists(int userId, int page, int pageSize) {
    PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
  
    Page<MediaList> userMediaLists;
    try {
      userMediaLists = mediaListRepository.getUserMediaLists(userId, pageRequest);
    } catch (Exception e) {
      System.out.println("Could not find user media lists: " + e.getMessage());
      throw new ResourceNotFoundException("Could not find user media lists.");
    }
  
    return userMediaLists;
  }


  @Override
  public MediaList getWatchlist(int watchlistId, int userId) throws Exception {
    MediaList watchlist;
    try {
      watchlist = findMediaList(watchlistId);
    } catch (Exception e) {
      System.out.println("Could not find watchlist: " + e.getMessage());
      throw new ResourceNotFoundException("Could not find watchlist.");
    }

    if (!watchlist.isWatchlist()) {
      throw new Exception("Cannot fetch list because it is not a watchlist");
    }

    Users listAuthor = watchlist.getUser();
    List<Integer> sharedWith = watchlist.getSharedWith();

    if (userId == listAuthor.getId()) {
      return watchlist;
    }

    if (sharedWith == null || sharedWith.isEmpty()) {
      throw new UnauthorizedException("You dont have access to this watchlist");
    }
    
    if (!sharedWith.contains(userId)) {
      throw new UnauthorizedException("You dont have access to this watchlist");
    }
    
    return watchlist;
  }

  @Override
  public Page<MediaList> getUserWatchlists(int userId, int page, int pageSize) {
    PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
  
    Page<MediaList> userWatchlists;
    try {
      userWatchlists = mediaListRepository.getUserWatchlists(userId, pageRequest);
    } catch (Exception e) {
      System.out.println("Could not find user watchlists: " + e.getMessage());
      throw new ResourceNotFoundException("Could not find user watchlists.");
    }
  
    return userWatchlists;
  }

  @Transactional
  @Override
  public List<Integer> updateWatchlistSharedWith(
    int watchlistId, 
    int userId, 
    List<Integer> userIds
    ) throws Exception {

    Users user = userService.findUser(userId);
    if (user == null) {
      System.out.println("Error: User not found");
      throw new ResourceNotFoundException("User not found");
    }

    MediaList mediaList = findMediaList(watchlistId);
    if (mediaList == null) {
      System.out.println("Error: Media list not found");
      throw new ResourceNotFoundException("Media list not found");
    }

    if (
      mediaList.isWatchlist() && 
      userIds != null
    ) {
      mediaList.setSharedWith(userIds);
    } else {
      throw new Exception("Cannot update user access. List is not watchlist.");
    }

    try {
      mediaListRepository.save(mediaList);
    } catch (Exception e) {
      System.out.println("Error occured while updating media list: " + e.getMessage());
      throw new Exception("Error occurred while updating media list.");
    }

    return userIds;
  }

}
