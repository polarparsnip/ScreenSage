package is.hi.screensage_web_server.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import is.hi.screensage_web_server.entities.MediaList;
import is.hi.screensage_web_server.entities.Review;
import is.hi.screensage_web_server.entities.Users;
import is.hi.screensage_web_server.interfaces.MediaServiceInterface;
import is.hi.screensage_web_server.interfaces.UserServiceInterface;
import is.hi.screensage_web_server.models.JwtPayload;
import is.hi.screensage_web_server.models.UserPrincipal;
import is.hi.screensage_web_server.models.UserRequest;
import is.hi.screensage_web_server.services.MediaListService;


/**
 * REST controller for managing user-related operations.
 *
 * The controller relies on the UserService to manage the business 
 * logic for all operations.
 */
@RestController
public class UserController {

  @Autowired
  private UserServiceInterface userService;

  @Autowired
  private MediaServiceInterface mediaService;

  @Autowired
  private MediaListService mediaListService;

  /**
   * Registers a new user with the provided username and password.
   *
   * @param username the username of the registering user
   * @param password the password of the registering user
   * @param image the optional profile image file of the registering user
   * @return a ResponseEntity containing the result of the registration process,
   *         including success or error information
   */
  @PostMapping("/register")
  public ResponseEntity<?> register(
    @RequestParam("username") String username,
    @RequestParam("password") String password,
    @RequestParam(value = "image", required = false) MultipartFile imageFile
  ) {
    Users newUser;

    if (imageFile == null || imageFile.isEmpty()) {
      newUser = userService.register(username, password);
    } else {
      newUser = userService.register(username, password, imageFile);
    }

    return ResponseEntity.ok(newUser);
  }

  /**
   * Authenticates a user with the provided username and password.
   *
   * @param userRequest the request body containing the username and password for login
   * @return a ResponseEntity containing the result of the login process,
   *         including a JWT token on successful authentication or an error message
   */
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody UserRequest userRequest) {
    String username = userRequest.getUsername();
    String password = userRequest.getPassword();
    JwtPayload jwtPayload = userService.login(username, password);
    return ResponseEntity.ok(jwtPayload);
  }

  /**
   * Retrieves the profile information for the currently authenticated user.
   *
   * @return a {@link ResponseEntity} containing the user's profile information
   */
  @GetMapping("/users/profile")
  public ResponseEntity<?> getUserProfile() {
    return ResponseEntity.ok(userService.getUserProfile()); 
  }

  /**
   * Retrieves a paginated list of reviews created by the currently authenticated user.
   *
   * @param page the page number to retrieve (default is 1)
   * @return a {@link ResponseEntity} containing a paginated list of the user's reviews
   */
  @GetMapping("/users/profile/reviews")
  public ResponseEntity<?> getUserReviews(
    @RequestParam(defaultValue = "1") int page
  ) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();
    int pageSize = 10;
    Page<Review> userReviews = mediaService.getUserReviews(userId, page, pageSize);
    return ResponseEntity.ok(userReviews);
  }
  
  /**
   * Updates the username of the currently authenticated user.
   *
   * @param update a {@link Map} containing the new username under the key "username"
   * @return a {@link ResponseEntity} indicating the result of the update operation
   */
  @PatchMapping("/users/profile/username")
  public ResponseEntity<?> updateUsername(
    @RequestBody Map<String, String> update
  ) {
    System.out.println(update);
    if (update.containsKey("username")) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
      int userId = authenticatedUser.getId();

      String newUsername = update.get("username");
      JwtPayload jwtPayload = userService.updateUsername(userId, newUsername);
      return ResponseEntity.ok(jwtPayload);
    }
    return ResponseEntity.badRequest().body("New Username required");
  }

  /**
   * Updates the password of the currently authenticated user.
   *
   * @param update a {@link Map} containing the new password under the key "password"
   * @return a {@link ResponseEntity} indicating the result of the update operation
   */
  @PatchMapping("/users/profile/password")
  public ResponseEntity<?> updatePassword(
    @RequestBody Map<String, String> update
  ) {
    if (update.containsKey("password")) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
      int userId = authenticatedUser.getId();

      String newPassword = update.get("password");
      Users updatedUser = userService.updatePassword(userId, newPassword);
      return ResponseEntity.ok(updatedUser);
    }
    return ResponseEntity.badRequest().body("New password required");
  }

  /**
   * Retrieves a paginated list of media lists for the authenticated user.
   *
   * @param page the page number to retrieve (default is 1)
   * @return a ResponseEntity containing the paginated list of media lists for the user
   */
  @GetMapping("users/profile/lists")
  public ResponseEntity<?> getUserLists(
    @RequestParam(defaultValue = "1") int page
  ) { 
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();
    int pageSize = 20;
    
    Page<MediaList> userMediaLists = mediaListService.getUserMediaLists(userId, page, pageSize);
    return ResponseEntity.ok(userMediaLists);
  }

}
