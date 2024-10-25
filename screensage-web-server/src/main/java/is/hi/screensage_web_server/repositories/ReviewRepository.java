package is.hi.screensage_web_server.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import is.hi.screensage_web_server.entities.Review;
import jakarta.transaction.Transactional;

/**
 * Repository interface for managing {@link Review} entities in the database.
 */
public interface ReviewRepository extends JpaRepository<Review, Integer> {

  /**
   * Retrieves a paginated list of reviews for a specific media of the specified type (tv or movie).
   *
   * @param type the type of the media (e.g., "movie" or "tv").
   * @param mediaId the ID of the media item.
   * @param pageable the pagination information.
   * @return a {@link Page} of {@link Review} objects, containing the reviews for the specified 
   *         media item for the specified page.
   */
  Page<Review> findByTypeAndMediaId(String type, int mediaId, Pageable pageable);
  
  /**
   * Calculates the average rating for a specific media item of the specified type.
   *
   * @param type the type of the media (e.g., "movie" or "tv").
   * @param mediaId the ID of the media item.
   * @return the average rating for the specified media item and type, 
   *         or null if no ratings exist.
   */
  @Query("SELECT AVG(r.rating) FROM Review r WHERE r.mediaId = :mediaId AND r.type = :type")
  Double getAverageRatingForMedia(String type, int mediaId);

  /**
   * Retrieves a list of recent reviews for a specific media item of the specified type.
   * Ordered by the creation date in descending order.
   *
   * @param type the type of the media (e.g., "movie" or "tv").
   * @param mediaId the ID of the media item.
   * @param pageable the pagination information.
   * @return {@link List} of {@link Review} objects, containing the recent reviews for the specified media item.
   */
  @Query("SELECT r FROM Review r WHERE r.mediaId = :mediaId AND r.type = :type ORDER BY r.createdAt DESC")
  List<Review> getRecentReviewsForMedia(String type, int mediaId, Pageable pageable);
  
  /**
   * Retrieves the rating given by a specific user for a specific media item of the specified type.
   *
   * @param userId the ID of the user.
   * @param type the type of the media (e.g., "movie" or "tv").
   * @param mediaId the ID of the media item.
   * @return the rating given by the user for the specified media item of the specified type, 
   *         or {@code null} if no rating exists.
   */
  @Query("SELECT r.rating FROM Review r WHERE r.user.id = :userId AND r.mediaId = :mediaId AND r.type = :type")
  Double getRatingByUserIdAndMediaIdAndType(int userId, String type, int mediaId);

  /**
   * Deletes a review by a specific user for a specific media item and type.
   *
   * @param userId the ID of the user who created the review
   * @param type the type of the media item (e.g., "movie" or "tv").
   * @param mediaId the ID of the media item
   * @return the number of reviews deleted, typically {@code 1} if successful 
   *         or {@code 0} if no matching review was found
   */
  @Modifying
  @Transactional
  @Query("DELETE FROM Review r WHERE r.user.id = :userId AND r.mediaId = :mediaId AND r.type = :type")
  int deleteReviewByUserIdAndMediaIdAndType(int userId, String type, int mediaId);

  /**
   * Retrieves a paginated list of reviews created by a specific user, 
   * ordered by creation date in descending order.
   *
   * @param userId the ID of the user whose reviews are to be retrieved
   * @param pageable a {@link Pageable} object specifying pagination and sorting details
   * @return a {@link Page} of {@link Review} objects, containing the user's reviews for the specified page
   */
  @Query("SELECT r FROM Review r WHERE r.user.id = :userId ORDER BY r.createdAt DESC")
  Page<Review> getUserReviews(int userId, Pageable pageable);
}
