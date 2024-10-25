package is.hi.screensage_web_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.screensage_web_server.entities.Users;

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

}
