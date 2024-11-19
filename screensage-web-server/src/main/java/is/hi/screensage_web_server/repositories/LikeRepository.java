package is.hi.screensage_web_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.screensage_web_server.entities.CompletedChallenge;
import is.hi.screensage_web_server.entities.Like;
import is.hi.screensage_web_server.entities.MediaList;
import is.hi.screensage_web_server.entities.Users;

/**
 * Repository interface for managing {@link CompletedChallenge} entities in the database.
 */
public interface LikeRepository extends JpaRepository<Like, Integer> {

  /**
   * Counts the number of likes for a specific media item.
   *
   * @param type    the type of the media (e.g., "movies", "shows", or "anime")
   * @param mediaId the unique ID of the media
   * @return        the count of likes for the specified media type and mediaId
   */
  long countByTypeAndMediaId(String type, int mediaId);

  /**
   * Counts the number of likes for a specific media list.
   *
   * @param mediaList the media list for which the like is for
   * @return          the count of likes for the specified media list
   */
  long countByMediaList(MediaList mediaList);

  /**
   * Checks if a like exists for a specific user, type, and mediaId.
   *
   * @param userId  the ID of the user who made the like
   * @param type    the type of the media (e.g., "movies", "shows", or "anime")
   * @param mediaId the unique ID of the media
   * @return        {@code true} if a like exists, {@code false} otherwise
   */
  boolean existsByUser_IdAndTypeAndMediaId(int userId, String type, int mediaId);

  /**
   * Checks if a like exists for a specific user (by userId) and media list.
   *
   * @param userId    the ID of the user who made the like
   * @param mediaList the media list the like is for
   * @return          {@code true} if a like exists, {@code false} otherwise
   */
  boolean existsByUser_IdAndMediaList(int userId, MediaList mediaList);

  /**
   * Deletes likes for a specific user, media type, and media ID.
   *
   * @param user    the user who made the like
   * @param type    the media type (e.g., "movies", "shows", "anime")
   * @param mediaId the ID of the media
   */
  void deleteByUserAndTypeAndMediaId(Users user, String type, int mediaId);
      
  /**
   * Deletes likes for a specific user and media list.
   *
   * @param user      the user who made the like
   * @param mediaList the media list to match
   */
  void deleteByUserAndMediaList(Users user, MediaList mediaList);
  
}
