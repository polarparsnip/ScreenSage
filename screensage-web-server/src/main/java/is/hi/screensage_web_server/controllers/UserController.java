package is.hi.screensage_web_server.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import is.hi.screensage_web_server.entities.Review;
import is.hi.screensage_web_server.entities.Users;
import is.hi.screensage_web_server.interfaces.MediaServiceInterface;
import is.hi.screensage_web_server.interfaces.UserServiceInterface;
import is.hi.screensage_web_server.models.UserPrincipal;
import is.hi.screensage_web_server.models.UserRequest;


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

    if (imageFile == null || imageFile.isEmpty()) {
      return userService.register(username, password);
    }

    return userService.register(username, password, imageFile);
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
    return userService.login(username, password); 
  }


  @GetMapping("/users/profile")
  public ResponseEntity<?> getUserProfile() {
    return ResponseEntity.ok(userService.getUserProfile()); 
  }


  @GetMapping("/users/profile/reviews")
  public ResponseEntity<?> getUserReviews(
    @RequestParam(defaultValue = "1") int Page
  ) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();
    int pageSize = 20;
    Page<Review> userReviews = mediaService.getUserReviews(userId, Page, pageSize);
    return ResponseEntity.ok(userReviews);
  }
  

  @PatchMapping("/users/profile/username")
  public ResponseEntity<?> updateUsername(
    @RequestBody Map<String, String> update
  ) {
    if (update.containsKey("username")) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
      int userId = authenticatedUser.getId();

      String newUsername = update.get("username");
      Users updatedUser = userService.updateUsername(userId, newUsername);
      return ResponseEntity.ok(updatedUser);
    }
    return ResponseEntity.badRequest().body("New Username required");
  }


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
}
