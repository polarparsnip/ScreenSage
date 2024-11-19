package is.hi.screensage_web_server.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import is.hi.screensage_web_server.entities.Users;
import is.hi.screensage_web_server.models.UserScore;

/**
 * Repository interface for accessing {@link Users} data in the database.
 */
public interface UserRepository extends JpaRepository<Users, Integer> {

  /**
   * Checks if a user with the given username exists in the database.
   *
   * @param username the username to check for existence
   * @return {@code true} if a user with the given username exists, {@code false} otherwise
   */
  boolean existsByUsername(String username);

  /**
   * Retrieves a user entity by its username.
   *
   * @param username the username of the user to retrieve
   * @return the {@link Users} entity associated with the given username, or null if no user found
   */
  Users findByUsername(String username);

  /**
   * Retrieves a paginated list of users and their scores ordered by their total points
   * accumulated from completed daily challenges.
   *
   * @param pageable the pagination information, including page number and size
   * @return a page of {@link UserScore} ordered by total points in descending order
   */
  @Query("SELECT new is.hi.screensage_web_server.models.UserScore(u, SUM(cc.points)) "
  + "FROM Users u "
  + "JOIN CompletedChallenge cc ON u.id = cc.user.id "
  + "GROUP BY u.id "
  + "ORDER BY SUM(cc.points) DESC, u.username ASC")
  Page<UserScore> findUsersOrderedByTotalPoints(Pageable pageable);

  /**
   * Retrieves a list of users whose usernames contain the specified query string.
   *
   * @param query the query string to search for in the usernames of the users
   * @return      a list of {@link Users} objects whose usernames contain the query string
   */
  List<Users> findByUsernameContainingIgnoreCase(String query);

}
