package is.hi.screensage_web_server.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import is.hi.screensage_web_server.config.CustomExceptions.InvalidInputException;
import is.hi.screensage_web_server.config.CustomExceptions.ResourceNotFoundException;
import is.hi.screensage_web_server.config.CustomExceptions.UnauthorizedException;
import is.hi.screensage_web_server.entities.Review;
import is.hi.screensage_web_server.entities.Users;
import is.hi.screensage_web_server.interfaces.MediaListServiceInterface;
import is.hi.screensage_web_server.interfaces.MediaServiceInterface;
import is.hi.screensage_web_server.interfaces.UserServiceInterface;
import is.hi.screensage_web_server.models.JwtPayload;
import is.hi.screensage_web_server.models.MediaListConcise;
import is.hi.screensage_web_server.models.UserPrincipal;
import is.hi.screensage_web_server.models.UserProfile;
import is.hi.screensage_web_server.models.UserScore;
import is.hi.screensage_web_server.repositories.UserRepository;
import jakarta.transaction.Transactional;

/**
 * Service for user-related services, including user registration and authentication.
 */
@Service
public class UserService implements UserServiceInterface {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthenticationManager authManager;

  @Autowired
  private JWTService jwtService;
  
  @Autowired
  private Cloudinary cloudinary;

  @Lazy
  @Autowired
  private MediaServiceInterface mediaService;

  @Lazy
  @Autowired
  private MediaListServiceInterface mediaListService;

  @Value("${user.default_profile_img}")
  private String defaultProfileImg;

  private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

  private final int MIN_USERNAME_LENGTH = 3;
  private final int MAX_USERNAME_LENGTH = 20;
  private final int MIN_PASSWORD_LENGTH = 2;
  private final int MAX_PASSWORD_LENGTH = 30;


  /**
   * Registers a new user with the provided username and password.
   *
   * Performs validation checks on the username and password, ensuring they are not empty, 
   * do not contain whitespace characters, and meet specified length requirements. 
   * If the username already exists in the repository, it returns a bad request response.
   * 
   * @param username the username of the new user
   * @param password the password of the new user
   * @return The newly created user object
   * @throws IOException 
   */
  @Override
  public Users register(String username, String password) {

    if (username == null || username.trim().isEmpty() || username.matches(".*\\s.*")) {
      System.out.println("Username can't be empty");
      throw new InvalidInputException("Username cannot be empty and must not contain whitespace characters.");
    }
    if (password == null || password.trim().isEmpty() || password.matches(".*\\s.*")) {
      System.out.println("Password can't be empty");
      throw new InvalidInputException("Password cannot be empty and must not contain whitespace characters.");
    }

    if (username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH) {
      System.out.println("Username must be between");
      throw new InvalidInputException("Username must be between " + MIN_USERNAME_LENGTH + " and " + MAX_USERNAME_LENGTH + " characters.");
    }
    if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
      System.out.println("Password must be between");
      throw new InvalidInputException("Password must be between " + MIN_PASSWORD_LENGTH + " and " + MAX_PASSWORD_LENGTH + " characters.");
    }

    if (userRepository.findByUsername(username) != null) {
      System.out.println("Username already exists");
      throw new InvalidInputException("Username already exists.");
    }

    Users newUser = new Users(username, encoder.encode(password), password.length());

    if (defaultProfileImg != null) {
      newUser.setProfileImg(defaultProfileImg);
    }

    try {
      userRepository.save(newUser);
    } catch (Exception e) {
      System.out.println("Error occured while saving user: " + e.getMessage());
      throw new RuntimeException("Error occurred while saving user.");
    }

    return newUser;
  }

  /**
   * Registers a new user with the provided username, password and profile image.
   *
   * Performs validation checks on the username and password, ensuring they are not empty, 
   * do not contain whitespace characters, and meet specified length requirements. 
   * If the username already exists in the repository, it returns a bad request response.
   * 
   * Then generates a url and id for the hosted image by uploading the it to cloudinary.
   * 
   * @param username the username of the new user
   * @param password the password of the new user
   * @param imageFile the profile image file of the new user
   * @return The newly created user object
   */
  @Override
  public Users register(String username, String password, MultipartFile imageFile) {

    if (username == null || username.trim().isEmpty() || username.matches(".*\\s.*")) {
      System.out.println("Username can't be empty");
      throw new InvalidInputException("Username cannot be empty and must not contain whitespace characters.");
    }
    if (password == null || password.trim().isEmpty() || password.matches(".*\\s.*")) {
      System.out.println("Password can't be empty");
      throw new InvalidInputException("Password cannot be empty and must not contain whitespace characters.");
    }

    if (username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH) {
      System.out.println("Username must be between");
      throw new InvalidInputException("Username must be between " + MIN_USERNAME_LENGTH + " and " + MAX_USERNAME_LENGTH + " characters.");
    }
    if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
      System.out.println("Password must be between");
      throw new InvalidInputException("Password must be between " + MIN_PASSWORD_LENGTH + " and " + MAX_PASSWORD_LENGTH + " characters.");
    }

    if (userRepository.findByUsername(username) != null) {
      System.out.println("Username already exists");
      throw new InvalidInputException("Username already exists.");
    }

    Users newUser = new Users(username, encoder.encode(password), password.length());

    try {
      @SuppressWarnings("rawtypes")
      Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
      String publicId = (String) uploadResult.get("public_id");
      String url = (String) uploadResult.get("secure_url");
      newUser.setProfileImgId(publicId);
      newUser.setProfileImg(url);
    } catch (IOException e) {
      ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while saving profile image.");
    }

    try {
      userRepository.save(newUser);
    } catch (Exception e) {
      System.out.println("Error occured while saving user: " + e.getMessage());
      throw new RuntimeException("Error occurred while saving user.");
    }

    return newUser;
  }

  /**
   * Authenticates a user using the provided username and password.
   *
   * If the authentication is successful, it generates a JWT token for the user and returns it in the response.
   * Performs validation on the username and password, ensuring they are not empty and do not contain whitespace characters.
   * 
   * @param username the username of the user attempting to log in
   * @param password the password of the user attempting to log in
   * @return JwtPayload containing the authorized user and a JWT token
   */
  @Override
  public JwtPayload login(String username, String password) { 
    if (username == null || username.trim().isEmpty() || username.matches(".*\\s.*")) {
      System.out.println("Username cannot be empty");
      throw new InvalidInputException("Username cannot be empty and must not contain whitespace characters.");
    }
    if (password == null || password.trim().isEmpty() || password.matches(".*\\s.*")) {
      System.out.println("Password cannot be empty");
      throw new InvalidInputException(("Password cannot be empty and must not contain whitespace characters."));
    }
    
    try {
      Authentication authentication = authManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, password)
      );

      String token = jwtService.generateToken(username);
      UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
      // System.out.println(authenticatedUser.getUser());

      Users user = authenticatedUser.getUser();
      try {
        List<MediaListConcise> mlc = mediaListService.getAllUserMediaListsConcise(user.getId(), false);
        List<MediaListConcise> wlc = mediaListService.getAllUserMediaListsConcise(user.getId(), true);
        user.setLists(mlc);
        user.setWatchlists(wlc);
      } catch (Exception e) {
        throw new RuntimeException(e.getMessage());
      }
      
      JwtPayload jwtPayload = new JwtPayload(authenticatedUser.getUser(), token);

      return jwtPayload;

    } catch (BadCredentialsException e) {
      throw new UnauthorizedException("Invalid username or password");
    }

  }

  
  @Override
  public UserProfile getUserProfile() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();

    Users user = findUser(userId);

    if (user == null) {
      throw new ResourceNotFoundException("User profile not found");
    }

    try {
      List<MediaListConcise> mlc = mediaListService.getAllUserMediaListsConcise(user.getId(), false);
      List<MediaListConcise> wlc = mediaListService.getAllUserMediaListsConcise(user.getId(), true);
      user.setLists(mlc);
      user.setWatchlists(wlc);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }

    int pageSize = 5;
    int page = 1;
    Page<Review> recentUserReviews = mediaService.getUserReviews(userId, page, pageSize);

    UserProfile userProfile = new UserProfile(user);

    if (recentUserReviews.hasContent()) {
      userProfile.setRecentReviews(recentUserReviews.getContent());
    }

    return userProfile;
  }

  @Transactional
  @Override
  public JwtPayload updateUsername(int userId, String newUsername) {
    if (newUsername == null || newUsername.trim().isEmpty() || newUsername.matches(".*\\s.*")) {
      throw new InvalidInputException(
        "Username cannot be empty and must not contain whitespace characters."
      );
    }
    if (newUsername.length() < MIN_USERNAME_LENGTH || newUsername.length() > MAX_USERNAME_LENGTH) {
      throw new InvalidInputException(
        "Username must be between " + MIN_USERNAME_LENGTH + " and " + MAX_USERNAME_LENGTH + " characters."
      );
    }
    if (userRepository.findByUsername(newUsername) != null) {
      throw new InvalidInputException("Username already exists.");
    }

    Users user = findUser(userId);

    if (user == null) {
      throw new ResourceNotFoundException("User not found");
    }

    user.setUsername(newUsername);

    try {
      userRepository.save(user);
    } catch (Exception e) {
      System.out.println("Error occured while saving user: " + e.getMessage());
      throw new RuntimeException("Error occurred while saving user.");
    }

    String token = jwtService.generateToken(user.getUsername());
    JwtPayload jwtPayload = new JwtPayload(user, token);

    return jwtPayload;
  }


  @Transactional
  @Override
  public Users updatePassword(int userId, String newPassword){
    if (newPassword == null || newPassword.trim().isEmpty() || newPassword.matches(".*\\s.*")) {
      throw new InvalidInputException(
        "Password cannot be empty and must not contain whitespace characters."
      );
    }
    if (newPassword.length() < MIN_PASSWORD_LENGTH || newPassword.length() > MAX_PASSWORD_LENGTH){
      throw new InvalidInputException(
        "Password must be between " + MIN_PASSWORD_LENGTH + " and " + MAX_PASSWORD_LENGTH + " characters."
      );
    }

    Users user = findUser(userId);

    if (user == null) {
      throw new ResourceNotFoundException("User not found");
    }

    user.setPasswordLength(newPassword.length());
    user.setPassword(encoder.encode(newPassword));

    try{
      userRepository.save(user);
    } catch (Exception e) {
      System.out.println("Error occured while saving user: " + e.getMessage());
      throw new RuntimeException("Error occurred while saving user.");
    }

    return user;
  }

  @Transactional
  @Override
  public Users updateProfileImage(int userId, MultipartFile imageFile) {
    Users user = findUser(userId);
    if (user == null) {
      throw new ResourceNotFoundException("User not found");
    }

    String publicId = user.getProfileImgId();
    Map<String, Object> uploadOptions = new HashMap<>();
    
    if (publicId != null) {
      uploadOptions.put("public_id", publicId);
      uploadOptions.put("overwrite", true);
      uploadOptions.put("invalidate", true);
    }

    try {
      @SuppressWarnings("rawtypes")
      Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), uploadOptions);
      String url = (String) uploadResult.get("secure_url");
      publicId = (String) uploadResult.get("public_id");
      user.setProfileImgId(publicId);
      user.setProfileImg(url);
    } catch (IOException e) {
      System.out.println("Error occurred while saving profile image: " + e.getMessage());
      ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while saving profile image.");
    }

    try{
      userRepository.save(user);
    } catch (Exception e) {
      System.out.println("Error occured while saving user: " + e.getMessage());
      throw new RuntimeException("Error occurred while saving user.");
    }

    return user;
  }

  @Override
  public Page<UserScore> getUserScoreboard(int page, int pageSize) {
    PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
    return userRepository.findUsersOrderedByTotalPoints(pageRequest);
  }

  @Override
  public boolean userExists(String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  public boolean userExists(int userId) {
    return userRepository.existsById(userId);
  }

  @Override
  public Users findUser(String username) {
    Users user = userRepository.findByUsername(username);
    return user;
  }

  @Override
  public Users findUser(int userId) {
    Optional<Users> user = userRepository.findById(userId);
    if (user.isPresent()) {
      return user.get();
    }

    return null;
  }

  @Override
  public Users getUserReferenceById(int userId) {
    Users user = userRepository.getReferenceById(userId);
    return user;
  }
  
  public List<Users> searchUsersByUsername(String query) {
    return userRepository.findByUsernameContainingIgnoreCase(query);
  }
}
