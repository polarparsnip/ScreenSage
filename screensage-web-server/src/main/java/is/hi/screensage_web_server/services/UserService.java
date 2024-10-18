package is.hi.screensage_web_server.services;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
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

import is.hi.screensage_web_server.config.CustomExceptions.ResourceNotFoundException;
import is.hi.screensage_web_server.entities.Review;
import is.hi.screensage_web_server.entities.Users;
import is.hi.screensage_web_server.interfaces.MediaServiceInterface;
import is.hi.screensage_web_server.interfaces.UserServiceInterface;
import is.hi.screensage_web_server.models.JwtPayload;
import is.hi.screensage_web_server.models.UserPrincipal;
import is.hi.screensage_web_server.models.UserProfile;
import is.hi.screensage_web_server.repositories.UserRepository;

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
   * @return ResponseEntity<?> containing the newly created user object, or an error message
   * @throws IOException 
   */
  @Override
  public ResponseEntity<?> register(String username, String password) {

    if (username == null || username.trim().isEmpty() || username.matches(".*\\s.*")) {
      System.out.println("Username can't be empty");
      return ResponseEntity.badRequest().body("Username cannot be empty and must not contain whitespace characters.");
    }
    if (password == null || password.trim().isEmpty() || password.matches(".*\\s.*")) {
      System.out.println("Password can't be empty");
      return ResponseEntity.badRequest().body("Password cannot be empty and must not contain whitespace characters.");
    }

    if (username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH) {
      System.out.println("Username must be between");
      return ResponseEntity.badRequest().body("Username must be between " + MIN_USERNAME_LENGTH + " and " + MAX_USERNAME_LENGTH + " characters.");
    }
    if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
      System.out.println("Password must be between");
      return ResponseEntity.badRequest().body("Password must be between " + MIN_PASSWORD_LENGTH + " and " + MAX_PASSWORD_LENGTH + " characters.");
    }

    if (userRepository.findByUsername(username) != null) {
      System.out.println("Username already exists");
      return ResponseEntity.badRequest().body("Username already exists.");
    }

    Users newUser = new Users(username, "");
    newUser.setPassword(encoder.encode(password));
    if (defaultProfileImg != null) {
      newUser.setProfileImg(defaultProfileImg);
    }

    try {
      userRepository.save(newUser);
    } catch (Exception e) {
      System.out.println("Error occured while saving user: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while saving user.");
    }

    return ResponseEntity.ok(newUser);
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
   * @return ResponseEntity<?> containing the newly created user object, or an error message
   */
  @Override
  public ResponseEntity<?> register(String username, String password, MultipartFile imageFile) {

    if (username == null || username.trim().isEmpty() || username.matches(".*\\s.*")) {
      System.out.println("Username can't be empty");
      return ResponseEntity.badRequest().body("Username cannot be empty and must not contain whitespace characters.");
    }
    if (password == null || password.trim().isEmpty() || password.matches(".*\\s.*")) {
      System.out.println("Password can't be empty");
      return ResponseEntity.badRequest().body("Password cannot be empty and must not contain whitespace characters.");
    }

    if (username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH) {
      System.out.println("Username must be between");
      return ResponseEntity.badRequest().body("Username must be between " + MIN_USERNAME_LENGTH + " and " + MAX_USERNAME_LENGTH + " characters.");
    }
    if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
      System.out.println("Password must be between");
      return ResponseEntity.badRequest().body("Password must be between " + MIN_PASSWORD_LENGTH + " and " + MAX_PASSWORD_LENGTH + " characters.");
    }

    if (userRepository.findByUsername(username) != null) {
      System.out.println("Username already exists");
      return ResponseEntity.badRequest().body("Username already exists.");
    }

    Users newUser = new Users(username, "");
    newUser.setPassword(encoder.encode(password));

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
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while saving user.");
    }

    return ResponseEntity.ok(newUser);
  }

  /**
   * Authenticates a user using the provided username and password.
   *
   * If the authentication is successful, it generates a JWT token for the user and returns it in the response.
   * Performs validation on the username and password, ensuring they are not empty and do not contain whitespace characters.
   * 
   * @param username the username of the user attempting to log in
   * @param password the password of the user attempting to log in
   * @return ResponseEntity<?> containing the JWT token, or an error message in case of failed authentication
   */
  @Override
  public ResponseEntity<?> login(String username, String password) { 
    if (username == null || username.trim().isEmpty() || username.matches(".*\\s.*")) {
      System.out.println("Username cannot be empty");
      return ResponseEntity.badRequest().body("Username cannot be empty and must not contain whitespace characters.");
    }
    if (password == null || password.trim().isEmpty() || password.matches(".*\\s.*")) {
      System.out.println("Password cannot be empty");
      return ResponseEntity.badRequest().body("Password cannot be empty and must not contain whitespace characters.");
    }
    
    try {
      Authentication authentication = authManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, password)
      );

      String token = jwtService.generateToken(username);
      UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
      // System.out.println(authenticatedUser.getUser());
      
      JwtPayload jwtPayload = new JwtPayload(authenticatedUser.getUser(), token);

      return ResponseEntity.ok(jwtPayload);

    } catch (BadCredentialsException e) {
        return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
        System.out.println("Error during login: " + e.getMessage());
        return new ResponseEntity<>("An error occurred during login", HttpStatus.INTERNAL_SERVER_ERROR);
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

    int pageSize = 5;
    int page = 1;
    Page<Review> recentUserReviews = mediaService.getUserReviews(userId, page, pageSize);

    UserProfile userProfile = new UserProfile(user);

    if (recentUserReviews.hasContent()) {
      userProfile.setRecentReviews(recentUserReviews.getContent());
    }

    return userProfile;
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
}
