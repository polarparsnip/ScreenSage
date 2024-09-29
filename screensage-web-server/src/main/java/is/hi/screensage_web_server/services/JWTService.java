package is.hi.screensage_web_server.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


/**
 * Service for generating and validating JSON Web Tokens (JWTs).
 * This class handles the creation, signing, and verification of JWTs
 * for user authentication and authorization.
 */
@Service
public class JWTService {
  private String secretKey = "";

  @Value("${jwt.expected.issuer}")
  private String expectedIssuer;

  @Value("${jwt.expected.audience}")
  private String expectedAudience;

  @Value("${jwt.expiration:3600000}")
  private int validity;

  /**
   * Constructor for JWTService. 
   * Generates a secret key for signing tokens.
   */
  public JWTService() {
    try {
      KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
      SecretKey sk = keyGen.generateKey();
      secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Gets the secret key used for signing and verifying JWTs.
   * 
   * @return SecretKey generated from the base64-encoded secretKey string variable
   */
  private SecretKey getKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  }

  /**
   * Generates a JWT token with the specified username as the subject.
   * Token contains claims for the issuer, audience, issued date, and expiration.
   * 
   * @param username the username to be included in the JWT as the subject
   * @return the generated JWT token as a String
   */
  public String generateToken(String username) {
    Map<String, Object> claims = new HashMap<>();
    return Jwts.builder()
      .claims()
      .add(claims)
      .subject(username)
      .issuer(expectedIssuer) 
      .audience().add(expectedAudience).and()
      .issuedAt(new Date(System.currentTimeMillis()))
      .expiration(new Date((new Date()).getTime() + validity))
      .and()
      .signWith(getKey())
      .compact();
  }

  /**
   * Extracts the username (subject) from the provided JWT token.
   * 
   * @param token the JWT token from which the username will be extracted
   * @return the username (subject) of the token
   */
  public String extractUserName(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Extracts a specific claim from the JWT token using the provided claim resolver function.
   * 
   * @param <T> the type of the claim to be extracted
   * @param token the JWT token from which the claim will be extracted
   * @param claimResolver a function to extract the claim from the token's claims
   * @return the extracted claim
   */
  private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
    final Claims claims = extractAllClaims(token);
    validateClaims(claims);
    return claimResolver.apply(claims);
  }

  /**
   * Parses the JWT token and extracts all its claims.
   * 
   * @param token the JWT token to be parsed
   * @return the claims contained in the JWT token
   */
  private Claims extractAllClaims(String token) {
    return Jwts.parser()
      .verifyWith(getKey())
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

  /**
   * Checks if the JWT token has expired.
   * 
   * @param token the JWT token to be checked
   * @return true if the token has expired, false otherwise
   */
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Extracts the expiration date of the JWT token.
   * 
   * @param token the JWT token from which the expiration date will be extracted
   * @return the expiration date of the token
   */
  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Validates the claims in the JWT token, ensuring the issuer and audience match expected values.
   * 
   * @param claims the claims to be validated
   * @throws RuntimeException if the issuer or audience are invalid
   */
  private void validateClaims(Claims claims) {
    if (!expectedIssuer.equals(claims.getIssuer())) {
      throw new RuntimeException("Invalid issuer");
    }
    if (!expectedAudience.equals(claims.getAudience().toArray()[0])) {
      System.out.println(claims.getAudience().toArray()[0]);
      throw new RuntimeException("Invalid audience");
    }
  }

  /**
   * Validates the JWT token against the provided user details.
   * 
   * The token is considered valid if the username from the token matches the username in the user details,
   * and the token has not expired.
   * 
   * @param token the JWT token to be validated
   * @param userDetails the user details used for validation
   * @return true if the token is valid, false otherwise
   */
  public boolean validateToken(String token, UserDetails userDetails) {
    final String userName = extractUserName(token);
    return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

}
